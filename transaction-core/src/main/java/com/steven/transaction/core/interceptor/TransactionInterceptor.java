package com.steven.transaction.core.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * TransactionInterceptor.
 *
 * @author steven
 */
@FunctionalInterface
public interface TransactionInterceptor {

    /**
     * Interceptor object.
     *
     * @param pjp
     * @return the object
     * @throws Throwable the throwable
     */
    Object interceptor(ProceedingJoinPoint pjp) throws Throwable;
}
