package com.steven.transaction.core.service;

import com.steven.transaction.common.context.TransactionContext;

/**
 * TransactionFactoryService.
 *
 * @author steven
 */
@FunctionalInterface
public interface TransactionFactoryService<T> {

    /**
     * product TransactionHandler.
     *
     * @param context {@linkplain TransactionContext}
     * @return {@linkplain TransactionHandler}
     * @throws Throwable ex
     */
    Class<T> factoryOf(TransactionContext context) throws Throwable;
}
