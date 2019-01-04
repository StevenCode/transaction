package com.steven.transaction.core.service;

import com.steven.transaction.common.config.TransactionConfig;

/**
 * InitService.
 *
 * @author steven
 */
@FunctionalInterface
public interface InitService {

    /**
     * init
     *
     * @param transactionConfig  {@linkplain TransactionConfig}
     */
    void initialization(TransactionConfig transactionConfig);
}
