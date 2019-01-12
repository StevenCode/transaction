package com.steven.transaction.core.service;

import com.steven.transaction.common.context.TransactionContext;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * TransactionHandler.
 *
 * @author steven
 */
@FunctionalInterface
public interface TransactionHandler {

    /**
     * TransactionHandler.
     *
     * @param point point
     * @param transactionContext {@linkplain TransactionContext}
     * @return Object
     * @throws Throwable ex
     */
    Object handler(ProceedingJoinPoint point, TransactionContext transactionContext) throws Throwable;
}
