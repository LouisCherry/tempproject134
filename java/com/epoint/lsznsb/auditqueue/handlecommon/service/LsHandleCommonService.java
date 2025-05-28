package com.epoint.lsznsb.auditqueue.handlecommon.service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.util.PinyinUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;

public class LsHandleCommonService
{
    IAuditOrgaWindow windowservice = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWindow.class);
    IHandleConfig handleConfigservice = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
    IHandleConfig configservice = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
    IMessagesCenterService messageservice = ContainerFactory.getContainInfo()
            .getComponent(IMessagesCenterService.class);
    IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);
    IAuditZnsbEquipment equipmentservice = ContainerFactory.getContainInfo().getComponent(IAuditZnsbEquipment.class);
    IAuditOrgaServiceCenter auditorgaserivce = ContainerFactory.getContainInfo()
            .getComponent(IAuditOrgaServiceCenter.class);
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public void SendQueueMessage(String QNO, String WindowGuid, String CenterGuid, String WindowNo) {
        // 发短信
        String fieldstr = " phone ";
        String Phone = "";
        String messagecontent = "";
        String ledcontent = "";
        String AS_IS_USE_LED = "";
        String location = "";
        AuditQueue QueueInfo = queueservice.getQNODetailByQNO(fieldstr, QNO, CenterGuid).getResult();
        if (QueueInfo != null) {
            Phone = QueueInfo.getPhone();
        }

        if (StringUtil.isNotBlank(Phone)) {
            // 如果值为0，则不发短信
            messagecontent = handleConfigservice.getFrameConfig("AS_QUEUE_MESSAGE_CALL", CenterGuid).getResult();
            if (StringUtil.isNotBlank(messagecontent)) {
                if (!QueueConstant.Common_no_String.equals(messagecontent)) {
                    AuditOrgaWindow window = windowservice.getWindowByWindowGuid(WindowGuid).getResult();
                    if (window != null) {
                        location = window.getStr("address");
                    }
                    messagecontent = messagecontent.replace("[#=Qno#]", QNO);
                    messagecontent = messagecontent.replace("[#=WindowNo#]", WindowNo);
                    messagecontent = messagecontent.replace("[#=Location#]", location);
                }
                else {
                    messagecontent = "";
                }
            }
            else {
                messagecontent = "请到" + WindowNo + "窗口办理。祝您愉快。";
            }

            if (StringUtil.isNotBlank(messagecontent)) {
                AuditOrgaServiceCenter center = auditorgaserivce.findAuditServiceCenterByGuid(CenterGuid).getResult();
                String areacode = "";
                if (center != null) {
                    areacode = center.getBelongxiaqu();
                }
                messageservice.insertSmsMessage(UUID.randomUUID().toString(), messagecontent, new Date(), 0, null,
                        Phone, UUID.randomUUID().toString(), "", "", "", "", "", null, false, areacode);
            }
        }

        // led显示
        AS_IS_USE_LED = configservice.getFrameConfig("AS_IS_USE_LED", CenterGuid).getResult();
        if (QueueConstant.CONSTANT_STR_ONE.equals(AS_IS_USE_LED)) {
            ledcontent = handleConfigservice.getFrameConfig("AS_QUEUE_LED_CALL", CenterGuid).getResult();
            if (StringUtil.isNotBlank(ledcontent)) {
                ledcontent = ledcontent.replace("[#=Qno#]", QNO);
                ledcontent = ledcontent.replace("[#=WindowNo#]", WindowNo);
            }
            else {
                ledcontent = "请" + QNO + "到" + WindowNo + "号窗口";
            }
            sendLEDMQ(ledcontent, WindowNo, CenterGuid);
        }

        // 语音叫号
        callByVoice(QNO, WindowNo, WindowGuid, CenterGuid);

    }

    // 提前发送短信
    public void tiqianSendMessage(String WindowGuid, String CenterGuid, String WindowNo, String UserGuid) {
        // 发短信
        String fieldstr = " phone,Is_TiQianSendSMS ";
        String phone = "";
        String messagecontent = "";
        String limitNumStr = "";
        List<String> qnoList = new ArrayList<String>();
        // 如果值为0，则不发短信
        messagecontent = handleConfigservice.getFrameConfig("AS_QUEUE_MESSAGE_TQ", CenterGuid).getResult();
        if (StringUtil.isNotBlank(messagecontent)) {
            if (QueueConstant.Common_no_String.equals(messagecontent)) {
                messagecontent = "";
            }
        }
        else {
            messagecontent = "温馨提示：将要到您办理业务，请耐心等待，祝您愉快。";
        }
        // 取出系统参数配置的AS_TiQianSendSMS的参数值
        limitNumStr = handleConfigservice.getFrameConfig("AS_TiQianSendSMS", CenterGuid).getResult();
        // 从临时表中获取qno的List集合
        if (StringUtil.isNotBlank(limitNumStr) && !QueueConstant.CONSTANT_STR_ZERO.equals(limitNumStr)) {
            qnoList = queueservice.getQnoList(WindowGuid, CenterGuid, UserGuid, Integer.valueOf(limitNumStr))
                    .getResult();
        }
        // 对每一个取出的编号都找到不为空的手机号，并发送一条短信提示
        if (qnoList != null) {
            for (String qno : qnoList) {
                AuditQueue queueInfo = queueservice.getQNODetailByQNO(fieldstr, qno, CenterGuid).getResult();
                if (queueInfo != null && !QueueConstant.CONSTANT_STR_ONE.equals(queueInfo.getIs_tiqiansendsms())) {
                    phone = queueInfo.getPhone();
                    if (StringUtil.isNotBlank(phone) && StringUtil.isNotBlank(messagecontent)) {
                        // 更新aduit_queue中字段 is_TiQiansendSMS状态为1
                        queueservice.updateTiQianSMSByQNO(qno, CenterGuid);
                        messageservice.insertSmsMessage(UUID.randomUUID().toString(), messagecontent, new Date(), 0,
                                null, phone, UUID.randomUUID().toString(), "", "", "", "", "", null, false, "");
                    }
                }
            }
        }
    }

    /**
     * 
     * 调用语音叫号
     * 
     */
    public void callByVoice(String QNO, String WindowNO, String WindowGuid, String centerguid) {
        AuditOrgaWindow window = windowservice.getWindowByWindowGuid(WindowGuid).getResult();
        if (window != null) {
            String hallguid = window.getLobbytype();
            String MacAddress = equipmentservice.selectMacAddressByLobbyType(hallguid).getResult();
            if (StringUtil.isNotBlank(MacAddress)) {
                sendVoiceMQ(QNO, MacAddress, WindowNO, centerguid);
            }
        }
    }

    public void sendVoiceMQ(String QNO, String MacAddress, String WindowNO, String centerguid) {
        String VoiceContent = "";
        VoiceContent = handleConfigservice.getFrameConfig("AS_QUEUE_VOICE_CALL", centerguid).getResult();
        if (StringUtil.isNotBlank(VoiceContent)) {
            VoiceContent = VoiceContent.replace("[#=Qno#]", QNO);
            VoiceContent = VoiceContent.replace("[#=WindowNo#]", WindowNO);
        }
        else {
            VoiceContent = "请" + QNO + "号到" + WindowNO + "号窗口办理";
        }

        String py = PinyinUtil.getFirstPinYinOfHanzi(VoiceContent);
        String QZ = py.substring(1, (QNO.length() - 2));

        JSONObject dataJson = new JSONObject();
        dataJson.put("methodName", "PlayText3");
        dataJson.put("sendContent", VoiceContent);
        dataJson.put("TerminalNo", MacAddress);
        dataJson.put("fileNames", QZ + "//" + py);
        try {
            // 梁山改为取号机发声
            ProducerMQ.send(MacAddress, "[" + VoiceContent + "]");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendLEDMQ(String content, String WindowNo, String CenterGuid) {
        try {
            JSONObject dataJson = new JSONObject();
            dataJson.put("regionName", WindowNo);
            dataJson.put("content", content);
            log.error("向窗口号" + WindowNo + ",发送led信息," + content);
            ProducerMQ.send("QueueLed_" + CenterGuid, "[" + dataJson.toString() + "]");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
