package com.epoint.jnzwfw.handlecommon.service;

import java.lang.invoke.MethodHandles;
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
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.util.PinyinUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;

public class JNHandleCommonService
{
    IAuditOrgaServiceCenter centerservice = ContainerFactory.getContainInfo()
            .getComponent(IAuditOrgaServiceCenter.class);
    IAuditOrgaWindow windowservice = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWindow.class);
    IHandleConfig handleConfigservice = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
    IHandleConfig configservice = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
    IMessagesCenterService messageservice = ContainerFactory.getContainInfo()
            .getComponent(IMessagesCenterService.class);
    IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);
    IAuditZnsbEquipment equipmentservice = ContainerFactory.getContainInfo().getComponent(IAuditZnsbEquipment.class);
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public void SendQueueMessage(String QNO, String WindowGuid, String CenterGuid, String WindowNo) {
        // 发短信
        String fieldstr = " phone,rowguid,is_hassendsms ";
        String Phone = "";
        String messagecontent = "";
        String ledcontent = "";
        String AS_IS_USE_LED = "";
        int isneedSend = 0;
        AuditQueue QueueInfo = queueservice.getQNODetailByQNO(fieldstr, QNO, CenterGuid).getResult();
        if (StringUtil.isNotBlank(QueueInfo)) {
            Phone = QueueInfo.getPhone();
            try {
                isneedSend = QueueInfo.getIs_hassendsms();
            }
            catch (Exception e) {

            }
        }

        if (StringUtil.isNotBlank(Phone) && isneedSend != 1) {
            // 如果值为0，则不发短信
            messagecontent = handleConfigservice.getFrameConfig("AS_QUEUE_MESSAGE_CALL", CenterGuid).getResult();
            if (StringUtil.isNotBlank(messagecontent)) {
                if (!QueueConstant.Common_no_String.equals(messagecontent)) {
                    messagecontent = messagecontent.replace("[#=Qno#]", QNO);
                    messagecontent = messagecontent.replace("[#=WindowNo#]", WindowNo);
                }
                else {
                    messagecontent = "";
                }
            }
            else {
                messagecontent = "请" + QNO + "到" + WindowNo + "号窗口办理。祝您愉快。";
            }
            if (StringUtil.isNotBlank(messagecontent)) {
                AuditOrgaServiceCenter center = centerservice.findAuditServiceCenterByGuid(CenterGuid).getResult();
                if (center != null) {
                    messageservice.insertSmsMessage(UUID.randomUUID().toString(), messagecontent, new Date(), 0, null,
                            Phone, UUID.randomUUID().toString(), "", "", "", "", "", null, false,
                            center.getBelongxiaqu());
                }
                else {
                    messageservice.insertSmsMessage(UUID.randomUUID().toString(), messagecontent, new Date(), 0, null,
                            Phone, UUID.randomUUID().toString(), "", "", "", "", "", null, false, "");
                }

                updateISHasSendSMS(QueueInfo.getRowguid());
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

    public void updateISHasSendSMS(String rowguid) {

        String sql = "update audit_queue set is_hassendsms = 1 where rowguid = ? ";

        ICommonDao dao = CommonDao.getInstance();
        dao.execute(sql, rowguid);
    }

    /**
     *
     * 调用语音叫号
     *
     */
    public void callByVoice(String QNO, String WindowNO, String WindowGuid, String centerguid) {

        AuditOrgaWindow window = windowservice.getWindowByWindowGuid(WindowGuid).getResult();
        if (StringUtil.isNotBlank(window)) {
            String hallguid = window.getLobbytype();
            // String MacAddress =
            // equipmentservice.selectMacAddressByLobbyType(hallguid).getResult();
            //
            // if (StringUtil.isNotBlank(MacAddress)) {
            //
            // sendVoiceMQ(QNO, MacAddress, WindowNO, centerguid);
            // }
            // 一个大厅多个终端，或者一个终端对应多个大厅
            List<String> list = equipmentservice.selectMacAddressByLikeLobbyType(hallguid).getResult();
            for (int i = 0; i < list.size(); i++) {
                if (StringUtil.isNotBlank(list.get(i))) {
                    sendVoiceMQ(QNO, list.get(i), WindowNO, centerguid);
                }
            }
        }
    };

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
        AuditZnsbEquipment result = equipmentservice.getDetailbyMacaddress(MacAddress).getResult();
        log.info("窗口号" + WindowNO + ",向[" + result.getMachinename() + "]功放,发送语音信息," + VoiceContent);
        String py = PinyinUtil.getFirstPinYinOfHanzi(VoiceContent);
        String QZ = py.substring(1, (QNO.length() - 2));

        JSONObject dataJson = new JSONObject();
        if ("5c405f9f-308a-4042-862a-036805819e9d".equals(centerguid)) {
            // system.out.println("----------金乡个性化语音叫号playertext4-----------");
            dataJson.put("methodName", "PlayText4");
        }
        else {
            dataJson.put("methodName", "PlayText3");
        }
        /* dataJson.put("methodName", "PlayText3"); */
        dataJson.put("sendContent", VoiceContent);
        dataJson.put("TerminalNo", MacAddress);
        dataJson.put("fileNames", QZ + "//" + py);
        try {
            if ("5e6cc7a7-14ac-4355-88a6-6c5db23f3829".equals(centerguid)) {
                ProducerMQ.send(MacAddress, "[" + VoiceContent + "]");
                log.info("梁山sendVoiceMQ MacAddress" + MacAddress + "VoiceContent" + VoiceContent);

                // 梁山改为取号机发声
            }
            else {
                ProducerMQ.send("QueueVoice_" + centerguid, "[" + dataJson.toString() + "]");
            }

        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void sendLEDMQ(String content, String WindowNo, String CenterGuid) {
        try {
            /*
             * String config = handleConfigservice.getFrameConfig(
             * "AS_ZNSB_INDIVIDUATION_SENDLEDMQ", CenterGuid)
             * .getResult();
             */

            JSONObject dataJson = new JSONObject();
            dataJson.put("regionName", WindowNo);
            dataJson.put("content", content);
            if ("5c405f9f-308a-4042-862a-036805819e9d".equals(CenterGuid)) {
                log.info("金乡向窗口号" + WindowNo + ",发送led信息," + content);
                ProducerMQ.send("QueueLed_" + CenterGuid + "00", "[" + dataJson.toString() + "]");
            }
            else {
                log.info("向窗口号" + WindowNo + ",发送led信息," + content);
                ProducerMQ.send("QueueLed_" + CenterGuid, "[" + dataJson.toString() + "]");
            }

        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
