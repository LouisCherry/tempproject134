package com.epoint.lsznsb.auditqueue.handlecommon.inter;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;

@Service
public interface LsIHandleCommon
{
    /**
     * 发送led信息(发送至MQ)
     * 
     * @param content
     *            内容
     * @param WindowNo
     *            窗口号
     * @param CenterGuid
     *            中心guid
     * @return
     */
    public AuditCommonResult<String> sendLEDMQ(String content, String WindowNo, String CenterGuid);

    /**
     * 发送语音，短信，led
     * 
     * @param QNO
     *            排队号
     * @param WindowGuid
     *            窗口guid
     * @param CenterGuid
     *            中心guid
     * @param WindowNo
     *            窗口号
     * @return
     */
    public AuditCommonResult<String> sendQueueMessage(String QNO, String WindowGuid, String CenterGuid,
            String WindowNo);

    /**
     * 提前发送短信
     * 
     * @param WindowGuid
     *            窗口guid
     * @param CenterGuid
     *            中心guid
     * @param WindowNo
     *            窗口号
     * @param UserGuid
     *            用户guid;
     * @return
     */
    public AuditCommonResult<String> tiqianSendMessage(String WindowGuid, String CenterGuid, String WindowNo,
            String UserGuid);

}
