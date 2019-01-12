package com.steven.transaction.core.service.impl;

import com.steven.transaction.common.context.TransactionContext;
import com.steven.transaction.common.enums.RoleEnum;
import com.steven.transaction.core.service.handler.ActorTransactionHandler;
import com.steven.transaction.core.service.handler.LocalTransactionHandler;
import com.steven.transaction.core.service.handler.StartTransactionHandler;
import com.steven.transaction.core.service.TransactionFactoryService;
import com.steven.transaction.core.service.engine.TransactionEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * TransactionFactoryServiceImpl.
 *
 * @author steven
 */
@Component
public class TransactionFactoryServiceImpl implements TransactionFactoryService {

    private final TransactionEngine transactionEngine;

    /**
     * Instantiates a new transaction factory service.
     *
     * @param transactionEngine
     */
    @Autowired
    public TransactionFactoryServiceImpl(final TransactionEngine transactionEngine) {
        this.transactionEngine = transactionEngine;
    }

    @Override
    public Class factoryOf(TransactionContext context) throws Throwable {
        if (!transactionEngine.isBegin()
                && Objects.isNull(context)) {
            return StartTransactionHandler.class;
        }else {
            if (context.getRole() == RoleEnum.LOCAL.getCode()) {
                return LocalTransactionHandler.class;
            }
            return ActorTransactionHandler.class;
        }
    }
}
