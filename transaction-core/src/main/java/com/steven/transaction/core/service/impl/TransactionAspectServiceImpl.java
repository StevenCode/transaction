package com.steven.transaction.core.service.impl;

import com.steven.transaction.common.context.TransactionContext;
import com.steven.transaction.core.helper.SpringBeanUtils;
import com.steven.transaction.core.service.TransactionAspectService;
import com.steven.transaction.core.service.TransactionFactoryService;
import com.steven.transaction.core.service.TransactionHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TransactionAspectServiceImpl.
 *
 * @author steven
 */
public class TransactionAspectServiceImpl implements TransactionAspectService {

    private final TransactionFactoryService transactionFactoryService;

    @Autowired
    public TransactionAspectServiceImpl(final TransactionFactoryServiceImpl transactionFactoryService) {
        this.transactionFactoryService = transactionFactoryService;
    }

    @Override
    public Object invoke(TransactionContext transactionContext, ProceedingJoinPoint point) throws Throwable {
        final Class clazz = transactionFactoryService.factoryOf(transactionContext);
        final TransactionHandler transactionHandler = (TransactionHandler) SpringBeanUtils.getInstance().getBean(clazz);
        return transactionHandler.handler(point, transactionContext);
    }
}
