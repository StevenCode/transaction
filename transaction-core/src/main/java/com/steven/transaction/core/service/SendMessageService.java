package com.steven.transaction.core.service;

import com.steven.transaction.common.bean.entity.Transaction;

/**
 * SendMessageService.
 *
 * @author steven
 */
public interface SendMessageService {

    /**
     *send message.
     * @param transaction
     * @return
     */
    Boolean sendMessage(Transaction transaction);
}
