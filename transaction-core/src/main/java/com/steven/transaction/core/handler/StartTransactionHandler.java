package com.steven.transaction.core.handler;

import com.steven.transaction.common.context.TransactionContext;
import com.steven.transaction.core.service.TransactionHandler;
import com.steven.transaction.core.service.engine.TransactionEngine;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * StartTransactionHandler.
 *
 * @author steven
 */
public class StartTransactionHandler implements TransactionHandler {

    private final TransactionEngine transactionEngine;

    /**
     *
     *
     * @param transactionEngine
     */
    public StartTransactionHandler(TransactionEngine transactionEngine) {
        this.transactionEngine = transactionEngine;
    }

    @Override
    public Object handler(ProceedingJoinPoint point, TransactionContext transactionContext) throws Throwable {
        return null;
    }
}
