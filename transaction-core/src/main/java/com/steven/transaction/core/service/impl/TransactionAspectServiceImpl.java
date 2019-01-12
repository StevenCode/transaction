package com.steven.transaction.core.service.impl;

import com.steven.transaction.common.context.TransactionContext;
import com.steven.transaction.core.service.TransactionAspectService;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * TransactionAspectServiceImpl.
 *
 * @author steven
 */
public class TransactionAspectServiceImpl implements TransactionAspectService {
    @Override
    public Object invoke(TransactionContext transactionContext, ProceedingJoinPoint point) throws Throwable {
        return null;
    }
}
