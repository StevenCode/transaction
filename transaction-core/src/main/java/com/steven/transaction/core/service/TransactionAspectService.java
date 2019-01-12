package com.steven.transaction.core.service;

import com.steven.transaction.common.context.TransactionContext;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * TransactionAspectService.
 *
 * @author steven
 */
@FunctionalInterface
public interface TransactionAspectService {

    /**
     * aspect.
     *
     * @param transactionContext {@linkplain TransactionContext}
     * @param point cut point.
     * @return object
     * @throws Throwable ex
     */
    Object invoke(TransactionContext transactionContext, ProceedingJoinPoint point) throws Throwable;
}
