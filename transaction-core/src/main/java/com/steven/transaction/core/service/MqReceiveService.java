package com.steven.transaction.core.service;

/**
 * MqReceiveService.
 *
 * @author steven
 */
@FunctionalInterface
public interface MqReceiveService {

    /**
     * process receive mq info.
     *
     * @param message
     * @return
     */
    Boolean processMessage(byte[] message);
}
