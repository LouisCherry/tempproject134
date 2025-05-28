package com.epoint.zoucheng.znsb.auditqueue.qhj.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlequeue.inter.IJNHandleQueue;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.zoucheng.znsb.auditqueue.qhj.api.IZCAuditQueue;

@RestController("zcteleinputlayerqueueaction")
@Scope("request")
public class ZCTeleInputLayerQueueAction extends BaseController
{
    private static final long serialVersionUID = 1223805361927646046L;
    @Autowired
    private IAuditQueueUserinfo userinfoservice;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IAuditQueue auditQueueservice;
    @Autowired
    private IJNHandleQueue handlequeueservice;

    @Autowired
    private IMessagesCenterService messageservice;
    @Autowired
    private IConfigService configService;
    @Autowired
    private IHandleConfig handleConfigService;
    @Autowired
    private IZCAuditQueue zcauditQueueservice;

    public void pageLoad() {
    }

    public void getPhone(String sfz) {
        if (StringUtil.isNotBlank(sfz)) {
            String Phone = "";
            Object mobile = this.userinfoservice.getPhonebyCardNO(sfz).getResult();
            if (StringUtil.isBlank(mobile)) {
                Phone = "";
            }
            else {
                Phone = mobile.toString();
            }

            this.addCallbackParam("phone", Phone);
        }

    }

    public void getQNO() {
        String phone = this.getRequestParameter("phone");
        String taskguid = this.getRequestParameter("taskguid");
        String sfz = this.getRequestParameter("sfz");
        String MacAddress = this.getRequestParameter("MacAddress");
        String centerguid = this.getRequestParameter("Centerguid");
        String hallguid = this.getRequestParameter("hallguid");
        boolean isBlackGetQno = false;
        Map<String, String> rtn = new HashMap<>(16);
        double alertlength = 0.0;
        int leftpaperpiece = 0;
        String operatorusermobile = "";

        AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(MacAddress,
                " Alertlength,Leftpaperpiece,operatorusermobile,MACHINENO,centerguid ").getResult();
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
        String useBlackList = this.configService.getFrameConfigValue("AS_IS_USE_BLACKLIST");
        if (StringUtil.isNotBlank(useBlackList) && "1".equals(useBlackList)) {
            AuditQueueUserinfo userinfo = (AuditQueueUserinfo) this.userinfoservice.getUserinfo(sfz).getResult();
            if (StringUtil.isNotBlank(userinfo)) {
                String is_foreverclose = userinfo.getIs_foreverclose();
                Date blacklisttodate = userinfo.getBlacklisttodate();
                if (StringUtil.isNotBlank(blacklisttodate) && "0".equals(is_foreverclose)) {
                    isBlackGetQno = true;
                    rtn.put("msg",
                            "您当前在黑名单中,无法取号,解禁时间为" + EpointDateUtil.convertDate2String(blacklisttodate, "yyyy-MM-dd"));
                }
                else if (StringUtil.isNotBlank(is_foreverclose) && "1".equals(is_foreverclose)) {
                    isBlackGetQno = true;
                    rtn.put("msg", "您当前在黑名单中永久禁用,无法取号");
                }
            }
        }

        // 获取人员可以取号的次数
        String count = handleConfigService.getFrameConfig("AS_ZNSB_ISLIMITCOUNT", equipment.getCenterguid())
                .getResult();
        if (StringUtil.isNotBlank(count) && StringUtil.isNotBlank(sfz)) {
            // 获取当前人员今天取号多少次
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            String FromDate = format.format(c.getTime());
            c.add(Calendar.DAY_OF_MONTH, 1);
            String ToDate = format.format(c.getTime());
            int ordercount = zcauditQueueservice.getQueueCountBySfz(sfz, FromDate, ToDate).getResult();
            if (ordercount >= Integer.valueOf(count)) {
                isBlackGetQno = true;
                rtn.put("msg", "您今日已取号" + ordercount + "次，已达今日取号上限，如需继续办理，请联系工作人员处理");
            }
        }

        if (!isBlackGetQno) {
            String existqno = (String) this.auditQueueservice.existQno(sfz, taskguid).getResult();
            if (StringUtil.isBlank(existqno)) {
                rtn = this.handlequeueservice.getQNO(sfz, phone, taskguid, centerguid, hallguid).getResult();
            }
            else {
                rtn.put("msg", "您有一个号码事项尚未办理，编号为" + existqno + ",请勿重复取号！");
            }
        }

        if (rtn.get("msg") != null && "success".equals(rtn.get("msg"))) {
            this.addCallbackParam("msg", rtn.get("msg"));
            this.addCallbackParam("qno", rtn.get("qno"));
        }
        else {
            this.addCallbackParam("msg", rtn.get("msg"));
        }

    }
}
