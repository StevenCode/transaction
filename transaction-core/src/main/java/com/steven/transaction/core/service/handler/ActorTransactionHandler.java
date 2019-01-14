package com.steven.transaction.core.service.handler;

import com.steven.transaction.common.context.TransactionContext;
import com.steven.transaction.common.enums.StatusEnum;
import com.steven.transaction.common.utils.LogUtil;
import com.steven.transaction.core.concurrent.threadlocal.TransactionContextLocal;
import com.steven.transaction.core.service.TransactionHandler;
import com.steven.transaction.core.service.engine.TransactionEngine;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ActorTransactionHandler.
 *
 * @author steven
 */
@Component
public class ActorTransactionHandler implements TransactionHandler {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ActorTransactionHandler.class);

    private final TransactionEngine transactionEngine;

    @Autowired
    public ActorTransactionHandler(final TransactionEngine transactionEngine) {
        this.transactionEngine = transactionEngine;
    }

    @Override
    public Object handler(ProceedingJoinPoint point, TransactionContext transactionContext) throws Throwable {
        try {
            //先保存事务日志
            transactionEngine.actorTransaction(point, transactionContext);
            //发起调用 执行try方法
            final Object proceed = point.proceed();
            //执行成功 更新状态为commit
            transactionEngine.updateStatus(StatusEnum.COMMIT.getCode());
            return proceed;
        } catch (Throwable throwable) {
            LogUtil.error(LOGGER, "执行分布式事务接口失败,事务id: {}",transactionContext::getTransId);
            transactionEngine.failTransaction(throwable.getMessage());
            throw throwable;
        }finally {
            TransactionContextLocal.getInstance().remove();
        }
    }
}
