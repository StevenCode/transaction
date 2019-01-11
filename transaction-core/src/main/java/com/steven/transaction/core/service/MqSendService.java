package com.steven.transaction.core.service;

/**
 * MqSendService.
 *
 * @author steven
 */
@FunctionalInterface
public interface MqSendService {

    /**
     * send message.
     * @param destination destination
     * @param pattern {@linkplain com.steven.transaction.annotation.MessageTypeEnum}
     * @param message convert MythTransaction to byte[]
     */
    void sendMessage(String destination, Integer pattern, byte[] message);
}
