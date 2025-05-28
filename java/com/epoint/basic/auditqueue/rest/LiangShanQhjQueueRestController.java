package com.epoint.basic.auditqueue.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.inter.JxIAuditQueueTasktype;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.lsznsb.auditqueue.handlequeue.inter.LsIHandleQueue;

@RestController
@RequestMapping("/liangshanqhjqueue")
public class LiangShanQhjQueueRestController
{
    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Autowired
    private IAuditQueueTasktype tasktypeservice;

    @Autowired
    private LsIHandleQueue lshandlequeueservice;

    @Autowired
    private IAuditQueueUserinfo userinfoservice;

    @Autowired
    private IAuditQueue queueservice;

    @Autowired
    private IMessagesCenterService messageservice;

    @Autowired
    private IConfigService configService;

    @Autowired
    private IHandleConfig handleConfigservice;

    @Autowired
    private JxIAuditQueueTasktype jxqueuetaskservice;

    /**
     * 取号
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getQno", method = RequestMethod.POST)
    public String getQno(@RequestBody String params, HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String cardno = obj.getString("cardno");
            String phone = obj.getString("phone");
            String tasktypeguid = obj.getString("tasktypeguid");
            String centerguid = obj.getString("centerguid");
            String hallguid = obj.getString("hallguid");
            String macaddress = obj.getString("macaddress");
            // 当前号是否是爱心取号模式
            String islove = obj.getString("islove");
            JSONObject dataJson = new JSONObject();

            double alertlength = 0.0;
            int leftpaperpiece = 0;
            String operatorusermobile = "";

            AuditZnsbEquipment equipment = equipmentservice
                    .getDetailbyMacaddress(macaddress, " Alertlength,Leftpaperpiece,operatorusermobile,MACHINENO")
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
            String useBlackList = configService.getFrameConfigValue("AS_IS_USE_BLACKLIST");
            if (StringUtil.isNotBlank(useBlackList) && QueueConstant.CONSTANT_STR_ONE.equals(useBlackList)) {
                // 获取当前身份证对应的用户信息
                AuditQueueUserinfo userinfo = userinfoservice.getUserinfo(cardno).getResult();
                if (StringUtil.isNotBlank(userinfo)) {
                    // 判断是永久禁用，还是封禁一段时间
                    String is_foreverclose = userinfo.getIs_foreverclose();
                    Date blacklisttodate = userinfo.getBlacklisttodate();
                    if (StringUtil.isNotBlank(blacklisttodate)
                            && QueueConstant.CONSTANT_STR_ZERO.equals(is_foreverclose)) {
                        return JsonUtils.zwdtRestReturn("0", "您当前在黑名单中,无法取号,解禁时间为"
                                + EpointDateUtil.convertDate2String(blacklisttodate, "yyyy-MM-dd"), "");
                    }
                    else if ((StringUtil.isNotBlank(is_foreverclose)
                            && QueueConstant.CONSTANT_STR_ONE.equals(is_foreverclose))) {
                        return JsonUtils.zwdtRestReturn("0", "您当前在黑名单中被永久禁用,无法取号", "");
                    }
                }
            }
            if (StringUtil.isNotBlank(cardno)) {
                AuditQueueTasktype tasktype = tasktypeservice.getAuditQueueTasktypeByRowguid(tasktypeguid).getResult();
                if ("1".equals(tasktype.getStr("issetlimit"))) {
                    String maxnumString = handleConfigservice.getFrameConfig("AS_IS_QNOMAXNUM", centerguid).getResult();
                    if (StringUtil.isNotBlank(maxnumString)) {
                        int maxnum = Integer.parseInt(maxnumString);
                        int nownumcount = jxqueuetaskservice.gettasktypesfzCount(tasktypeguid, cardno);
                        if (maxnum <= nownumcount) {
                            return JsonUtils.zwdtRestReturn("0", "您今天本事项分类的号已到上限，", "");
                        }
                    }

                }
            }

            String existqno = queueservice.existQno(cardno, tasktypeguid).getResult();
            if (StringUtil.isBlank(existqno)) {
                Map<String, String> msg = new HashMap<String, String>();
                msg = lshandlequeueservice.getQNO(cardno, phone, tasktypeguid, centerguid, hallguid, islove)
                        .getResult();
                if (msg.get("msg") != null && "success".equals(msg.get("msg"))) {
                    dataJson.put("qno", msg.get("qno"));
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", msg.get("msg"), "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "您有一个号码事项尚未办理，编号为" + existqno + ",请勿重复取号！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e, "");
        }

    }

}
