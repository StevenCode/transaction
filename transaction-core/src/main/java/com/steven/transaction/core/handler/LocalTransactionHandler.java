package com.steven.transaction.core.handler;

import com.steven.transaction.common.context.TransactionContext;
import com.steven.transaction.core.service.TransactionHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

/**
 * LocalTransactionHandler.
 *
 * @author steven
 */
@Component
public class LocalTransactionHandler implements TransactionHandler {

    @Override
    public Object handler(ProceedingJoinPoint point, TransactionContext transactionContext) throws Throwable {
        return point.proceed();
    }
}
