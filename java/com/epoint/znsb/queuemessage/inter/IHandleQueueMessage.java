package com.epoint.znsb.queuemessage.inter;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

@Service
public interface IHandleQueueMessage
{
    Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 发送短信消息
     *
     * @param QNO
     * @param WindowN0
     * @param CenterGuid
     * @param WindowN0
     * @return
     */
    AuditCommonResult<String> sendQueueMessage(String QNO, String WindowN0, String CenterGuid, String smstype,
            Record record);

    /**
     * 发送LED消息
     *
     * @param QNO
     * @param WindowN0
     * @param CenterGuid
     * @param WindowStatus
     * @return
     */
    AuditCommonResult<String> sendLEDMQ(String QNO, String WindowN0, String CenterGuid, String WindowStatus);

    /**
     * 发送广播消息
     *
     * @param QNO
     * @param WindowNO
     * @param WindowGuid
     * @param CenterGuid
     * @return
     */
    AuditCommonResult<String> callByVoice(String QNO, String WindowNO, String WindowGuid, String CenterGuid);

    /**
     * 发送事项分类预警短信
     * 
     * @param tasktypeguid
     * @param centerguid
     * @return
     */
    void sendTaskTypeEwMessage(Integer count, String tasktypeguid, String centerguid);

}
