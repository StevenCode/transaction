package com.steven.transaction.core.service.mq.receive;

import com.steven.transaction.common.bean.entity.Invocation;
import com.steven.transaction.common.bean.entity.Transaction;
import com.steven.transaction.common.bean.mq.MessageEntity;
import com.steven.transaction.common.config.TransactionConfig;
import com.steven.transaction.common.context.TransactionContext;
import com.steven.transaction.common.enums.EventTypeEnum;
import com.steven.transaction.common.enums.RoleEnum;
import com.steven.transaction.common.enums.StatusEnum;
import com.steven.transaction.common.exception.TransactionException;
import com.steven.transaction.common.exception.TransactionRuntimeException;
import com.steven.transaction.common.serializer.ObjectSerializer;
import com.steven.transaction.common.utils.LogUtil;
import com.steven.transaction.core.concurrent.threadlocal.TransactionContextLocal;
import com.steven.transaction.core.coordinator.CoordinatorService;
import com.steven.transaction.core.disruptor.publisher.TransactionEventPublisher;
import com.steven.transaction.core.helper.SpringBeanUtils;
import com.steven.transaction.core.service.MqReceiveService;
import com.steven.transaction.core.service.mq.send.SendMessageServiceImpl;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MqReceiveServiceImpl.
 *
 * @author steven
 */
@Service("mqReceiveService")
public class MqReceiveServiceImpl implements MqReceiveService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MqReceiveServiceImpl.class);

    private static final Lock LOCK = new ReentrantLock();

    private volatile ObjectSerializer serializer;

    @Autowired
    private CoordinatorService coordinatorService;

    @Autowired
    private TransactionEventPublisher publisher;

    @Autowired
    private TransactionConfig transactionConfig;

    @Override
    public Boolean processMessage(byte[] message) {
        try {
            MessageEntity entity;
            try {
                entity = getObjectSerializer().deSerialize(message, MessageEntity.class);
            } catch (TransactionException e) {
                e.printStackTrace();
                throw new TransactionRuntimeException(e.getMessage());
            }
            /* 1 检查该事务有没有被处理过，已经处理过 则不处理
             * 2 发起发射调用，调用接口，进行处理
             * 3 记录本地日志
             */
            LOCK.lock();
            final String transId = entity.getTransId();
            final Transaction transaction = coordinatorService.findByTransId(transId);
            if (Objects.isNull(transaction)) {
                try {
                    execute(entity);
                    //执行成功 保存成功日志
                    final Transaction log = buildTransactionLog(transId, "",
                            StatusEnum.COMMIT.getCode(),
                            entity.getInvocation().getTargetClass().getName(),
                            entity.getInvocation().getMethodName());
                    publisher.publishEvent(log, EventTypeEnum.SAVE.getCode());
                } catch (Exception e) {
                    //执行失败保存失败的日志
                    final Transaction log = buildTransactionLog(transId, e.getMessage(),
                            StatusEnum.FAILURE.getCode(),
                            entity.getInvocation().getTargetClass().getName(),
                            entity.getInvocation().getMethodName());
                    publisher.publishEvent(log, EventTypeEnum.SAVE.getCode());
                    throw new TransactionRuntimeException(e);
                }finally {
                    TransactionContextLocal.getInstance().remove();
                }
            }else {
                //如果是执行失败的话
                if (transaction.getStatus() == StatusEnum.FAILURE.getCode()) {
                    //如果超过最大重试次数 则不执行
                    if (transaction.getRetriedCount() >= transactionConfig.getRetryMax()) {
                        LogUtil.error(LOGGER, () -> "此事务已经超过了最大重试次数:" + transactionConfig.getRetryMax()
                                + " ,执行接口为:" + entity.getInvocation().getTargetClass() + " ,方法为:"
                                + entity.getInvocation().getMethodName() + ",事务id为：" + entity.getTransId());
                        return Boolean.FALSE;
                    }
                    try {
                        execute(entity);
                        //执行成功 更新日志为成功
                        transaction.setStatus(StatusEnum.COMMIT.getCode());
                        publisher.publishEvent(transaction, EventTypeEnum.UPDATE_STATUS.getCode());
                    } catch (Throwable e) {
                        //执行失败,设置失败原因和重试次数
                        transaction.setErrorMsg(e.getCause().getMessage());
                        transaction.setRetriedCount(transaction.getRetriedCount() + 1);
                        publisher.publishEvent(transaction, EventTypeEnum.UPDATE_FAIR.getCode());
                        throw new TransactionRuntimeException(e);
                    }finally {
                        TransactionContextLocal.getInstance().remove();
                    }
                }
            }
        } finally {
            LOCK.unlock();
        }
        return Boolean.TRUE;
    }

    private void execute(final MessageEntity entity) throws Exception {
        //设置事务上下文，这个类会传递到远端
        TransactionContext context = new TransactionContext();
        //设置事务id
        context.setTransId(entity.getTransId());
        //设置为发起者角色
        context.setRole(RoleEnum.LOCAL.getCode());
        TransactionContextLocal.getInstance().set(context);
        executeLocalTransaction(entity.getInvocation());
    }

    private void executeLocalTransaction(final Invocation invocation) throws Exception {
        if (Objects.isNull(invocation)) {
            final Class clazz = invocation.getTargetClass();
            final String method = invocation.getMethodName();
            final Object[] args = invocation.getArgs();
            final Class[] parameterTypes = invocation.getParameterTypes();
            final Object bean = SpringBeanUtils.getInstance().getBean(clazz);
            MethodUtils.invokeMethod(bean, method, args, parameterTypes);
            LogUtil.debug(LOGGER, "Myth执行本地协调事务:{}", () -> invocation.getTargetClass() + ":" + invocation.getMethodName());
        }
    }

    private Transaction buildTransactionLog(final String transId, final String errorMsg, final Integer status,
                                            final String targetClass, final String targetMethod) {
        Transaction transaction = new Transaction(transId);
        transaction.setRetriedCount(1);
        transaction.setStatus(status);
        transaction.setErrorMsg(errorMsg);
        transaction.setRole(RoleEnum.PROVIDER.getCode());
        transaction.setTargetClass(targetClass);
        transaction.setTargetMethod(targetMethod);
        return transaction;
    }

    private synchronized ObjectSerializer getObjectSerializer() {
        if (serializer == null) {
            synchronized (SendMessageServiceImpl.class) {
                if (serializer == null) {
                    serializer = SpringBeanUtils.getInstance().getBean(ObjectSerializer.class);
                }
            }
        }
        return serializer;
    }
}
