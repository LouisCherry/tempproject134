package com.epoint.auditqueue.qhj.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.composite.auditqueue.handlequeue.inter.IJNHandleQueue;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

@RestController("jnteleinputlayerqueueaction")
@Scope("request")
public class JNTeleInputLayerQueueAction extends BaseController
{

    private static final long serialVersionUID = 1223805361927646046L;
    @Autowired
    private IAuditQueueUserinfo userinfoservice;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IAuditQueue auditQueueservice;
    @Autowired
    private IHandleQueue handlequeueservice;
    @Autowired
    private IJNHandleQueue jnhandlequeueservice;
    @Autowired
    private IMessagesCenterService messageservice;

    @Autowired
    private IConfigService epointConfigService;
    @Autowired
    private IHandleConfig handleConfig;

    @Override
    public void pageLoad() {

    }

    public void getPhone(String sfz) {
        if (StringUtil.isNotBlank(sfz)) {
            String Phone = "";
            Object mobile = userinfoservice.getPhonebyCardNO(sfz).getResult();
            if (StringUtil.isBlank(mobile)) {
                Phone = "";
            }
            else {
                Phone = mobile.toString();
            }
            addCallbackParam("phone", Phone);
        }
    }

    public void getQNO() {
        String phone = getRequestParameter("phone");
        String taskguid = getRequestParameter("taskguid");
        String sfz = getRequestParameter("sfz");
        String MacAddress = getRequestParameter("MacAddress");
        String centerguid = getRequestParameter("Centerguid");
        String hallguid = getRequestParameter("hallguid");
        String islove = getRequestParameter("islove");
        // 判断是否时黑名单用户取号
        boolean isBlackGetQno = false;
        Map<String, String> rtn = new HashMap<String, String>(16);

        double alertlength = 0.0;
        int leftpaperpiece = 0;
        String operatorusermobile = "";

        AuditZnsbEquipment equipment = equipmentservice
                .getDetailbyMacaddress(MacAddress, " Alertlength,Leftpaperpiece,operatorusermobile,MACHINENO")
                .getResult();
        if (StringUtil.isNotBlank(equipment)) {
            alertlength = StringUtil.isBlank(equipment.getAlertlength()) ? 0.0 : equipment.getAlertlength();
            leftpaperpiece = StringUtil.isBlank(equipment.getLeftpaperpiece()) ? 0 : equipment.getLeftpaperpiece();
            operatorusermobile = equipment.getOperatorusermobile();
            // 发送预警短信
            if (StringUtil.isNotBlank(operatorusermobile)) {
                if (leftpaperpiece == 1) {
                    messageservice.insertSmsMessage(UUID.randomUUID().toString(),
                            "设备编号：" + equipment.getMachineno() + "，剩余纸张数量为0，请管理员赶紧添加纸张！", new Date(), 0, new Date(),
                            operatorusermobile, UUID.randomUUID().toString(), "", "", "", "", "", "", false, "");
                }
                if (leftpaperpiece == alertlength + 1) {
                    messageservice.insertSmsMessage(UUID.randomUUID().toString(),
                            "设备编号：" + equipment.getMachineno() + "，剩余纸张数量已达报警长度，请管理员注意！", new Date(), 0, new Date(),
                            operatorusermobile, UUID.randomUUID().toString(), "", "", "", "", "", "", false, "");
                }
            }
        }

        // 判断黑名单功能是否开启
        String useBlackList = epointConfigService.getFrameConfigValue("AS_IS_USE_BLACKLIST");
        if (StringUtil.isNotBlank(useBlackList) && QueueConstant.CONSTANT_STR_ONE.equals(useBlackList)) {
            // 获取当前身份证对应的用户信息
            AuditQueueUserinfo userinfo = userinfoservice.getUserinfo(sfz).getResult();
            if (StringUtil.isNotBlank(userinfo)) {
                // 判断是永久禁用，还是封禁一段时间
                String is_foreverclose = userinfo.getIs_foreverclose();
                Date blacklisttodate = userinfo.getBlacklisttodate();
                if (StringUtil.isNotBlank(blacklisttodate) && QueueConstant.CONSTANT_STR_ZERO.equals(is_foreverclose)) {
                    isBlackGetQno = true;
                    rtn.put("msg",
                            "您当前在黑名单中,无法取号,解禁时间为" + EpointDateUtil.convertDate2String(blacklisttodate, "yyyy-MM-dd"));
                }
                else if ((StringUtil.isNotBlank(is_foreverclose)
                        && QueueConstant.CONSTANT_STR_ONE.equals(is_foreverclose))) {
                    isBlackGetQno = true;
                    rtn.put("msg", "您当前在黑名单中永久禁用,无法取号");
                }
            }

        }
        if (!isBlackGetQno) {
            // 没有开启黑名单
            String existqno = auditQueueservice.existQno(sfz, taskguid).getResult();
            if (StringUtil.isBlank(existqno)) {
                rtn = jnhandlequeueservice.getQNO(sfz, phone, taskguid, centerguid, hallguid, islove).getResult();
            }
            else {
                rtn.put("msg", "您有一个号码事项尚未办理，编号为" + existqno + ",请勿重复取号！");
            }
        }

        if (rtn.get("msg") != null && "success".equals(rtn.get("msg"))) {
            addCallbackParam("msg", rtn.get("msg"));
            addCallbackParam("qno", rtn.get("qno"));
        }
        else {
            addCallbackParam("msg", rtn.get("msg"));
        }

    }

}
