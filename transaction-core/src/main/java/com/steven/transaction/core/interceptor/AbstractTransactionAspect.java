package com.steven.transaction.core.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * AbstractTransactionAspect.
 *
 * @author steven
 */
@Aspect
public abstract class AbstractTransactionAspect {

    private TransactionInterceptor transactionInterceptor;

    /**
     * set transactionInterceptor.
     *
     * @param transactionInterceptor
     */
    protected void setTransactionInterceptor(final TransactionInterceptor transactionInterceptor) {
        this.transactionInterceptor = transactionInterceptor;
    }

    /**
     * this is point cut with {@linkplain com.steven.transaction.annotation.Transaction}
     */
    @Pointcut("@annotation(com.steven.transaction.annotation.Transaction)")
    public void transactionInterceptor() {

    }

    /**
     * this is around in {@linkplain com.steven.transaction.annotation.Transaction}
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("transactionInterceptor()")
    public Object interceptAnnotationMethod(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return transactionInterceptor.interceptor(proceedingJoinPoint);
    }

    /**
     * spring bean Order.
     *
     * @return int order
     */
    public abstract int getOrder();
}
