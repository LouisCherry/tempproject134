package com.epoint.composite.auditqueue.jxhandlecommon.inter;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;

@Service
public interface ISSHandleCommon
{

    /**
     * 发送led信息(请勿直接调用，服务调用该方法，防止led服务停止，影响程序)
     * @param content 内容
     * @param WindowNo 窗口号
     * @param CenterGuid 中心guid
     * @return
     */
    /*
    public AuditCommonResult<String> sendLED(String content,String WindowNo,String CenterGuid);

    *//**
      * 发送语音信息(请勿直接调用，服务调用该方法，防止语音服务停止，影响程序)
      * @param QNO 排队号
      * @param MacAddress 终端号
      * @param WindowNO 窗口号
      * @param centerguid 中心guid
      * @return
      *//*
        public AuditCommonResult<String> sendVoice(String QNO, String MacAddress, String WindowNO, String centerguid);*/
    /**
     * 发送led信息(发送至MQ)
     * @param content 内容
     * @param WindowNo 窗口号
     * @param CenterGuid 中心guid
     * @return
     */
    public AuditCommonResult<String> sendLEDMQ(String content, String WindowNo, String CenterGuid);

    /**
     * 发送语音，短信，led
     * @param QNO 排队号
     * @param WindowGuid 窗口guid
     * @param CenterGuid 中心guid
     * @param WindowNo 窗口号
     * @return
     */
    public AuditCommonResult<String> sendQueueMessage(String QNO, String WindowGuid, String CenterGuid,
                                                      String WindowNo);

    /**
     * 提前发送短信
     * @param WindowGuid 窗口guid
     * @param CenterGuid 中心guid
     * @param WindowNo 窗口号
     * @param UserGuid  用户guid;
     * @return
     */
    public AuditCommonResult<String> tiqianSendMessage(String WindowGuid, String CenterGuid, String WindowNo,
                                                       String UserGuid);

}
