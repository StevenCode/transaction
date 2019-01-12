package com.steven.transaction.core.service.handler;

import com.steven.transaction.common.context.TransactionContext;
import com.steven.transaction.common.enums.StatusEnum;
import com.steven.transaction.core.concurrent.threadlocal.TransactionContextLocal;
import com.steven.transaction.core.service.TransactionHandler;
import com.steven.transaction.core.service.engine.TransactionEngine;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * StartTransactionHandler.
 *
 * @author steven
 */
public class StartTransactionHandler implements TransactionHandler {

    private final TransactionEngine transactionEngine;

    @Autowired
    public StartTransactionHandler(TransactionEngine transactionEngine) {
        this.transactionEngine = transactionEngine;
    }

    @Override
    public Object handler(ProceedingJoinPoint point, TransactionContext transactionContext) throws Throwable {
        try {
            transactionEngine.begin(point);
            final Object proceed = point.proceed();
            transactionEngine.updateStatus(StatusEnum.COMMIT.getCode());
            return proceed;
        } catch (Throwable throwable) {
            //更新失败的日志信息
            transactionEngine.failTransaction(throwable.getMessage());
            throw throwable;
        }finally {
            //发送消息
            transactionEngine.sendMessage();
            transactionEngine.cleanThreadLocal();
            TransactionContextLocal.getInstance().remove();
        }
    }
}
