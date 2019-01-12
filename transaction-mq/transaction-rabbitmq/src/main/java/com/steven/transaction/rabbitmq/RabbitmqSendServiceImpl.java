package com.steven.transaction.rabbitmq;

import com.steven.transaction.common.utils.LogUtil;
import com.steven.transaction.core.service.MqSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * RabbitmqSendServiceImpl.
 *
 * @author steven
 */
public class RabbitmqSendServiceImpl implements MqSendService, RabbitTemplate.ConfirmCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitmqSendServiceImpl.class);

    private AmqpTemplate amqpTemplate;

    public void setAmqpTemplate(final AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public void sendMessage(String destination, Integer pattern, byte[] message) {
        amqpTemplate.convertAndSend(destination, message);
    }


    /**
     * Confirmation callback.
     * 消息的回调，主要是实现RabbitTemplate.ConfirmCallback接口
     * 注意，消息回调只代表成功消息发送到RabbitMQ服务器，不能代表消息成功被处理和接受
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            LogUtil.info(LOGGER, () -> "rabbit mq send message success！");
        }else {
            LogUtil.info(LOGGER, () -> "rabbit mq send message fail！" + cause + " retry send!");
        }
    }
}
