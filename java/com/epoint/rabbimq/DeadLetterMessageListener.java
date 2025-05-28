package com.epoint.rabbimq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;


public class DeadLetterMessageListener implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(DeadLetterMessageListener.class);

    @Override
    public void onMessage(Message message) {
        String messageBody = new String(message.getBody());
        logger.info("Received dead letter message: {}", messageBody);
        // 可以添加更多的日志信息，如消息属性等
        logger.info("Message properties: {}", message.getMessageProperties());
        // 处理死信消息的逻辑，例如重新发送、存储等
    }
}
