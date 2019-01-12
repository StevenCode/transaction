package com.steven.transaction.core.service.engine;

import com.steven.transaction.common.bean.entity.Participant;
import com.steven.transaction.common.bean.entity.Transaction;
import com.steven.transaction.common.context.TransactionContext;
import com.steven.transaction.common.enums.EventTypeEnum;
import com.steven.transaction.common.enums.RoleEnum;
import com.steven.transaction.common.enums.StatusEnum;
import com.steven.transaction.common.utils.LogUtil;
import com.steven.transaction.core.concurrent.threadlocal.TransactionContextLocal;
import com.steven.transaction.core.disruptor.publisher.TransactionEventPublisher;
import com.steven.transaction.core.service.SendMessageService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * TransactionEngine.
 *
 * @author steven
 */
@Component
@SuppressWarnings("unchecked")
public class TransactionEngine {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionEngine.class);

    /**
     * save Transaction in threadLocal.
     */
    private static final ThreadLocal<Transaction> CURRENT = new ThreadLocal<>();

    private final SendMessageService sendMessageService;

    private final TransactionEventPublisher publisher;

    @Autowired
    public TransactionEngine(SendMessageService sendMessageService, TransactionEventPublisher publisher) {
        this.sendMessageService = sendMessageService;
        this.publisher = publisher;
    }

    /**
     * this is starter begin Transaction.
     *
     * @param point cut point.
     */
    public void begin(final ProceedingJoinPoint point) {
        LogUtil.debug(LOGGER, () -> "开始执行Transaction分布式事务!start");
        Transaction transaction = buildTransaction(point, RoleEnum.START.getCode(), StatusEnum.BEGIN.getCode(), "");
        //发布事务保存事件，异步保存
        publisher.publishEvent(transaction, EventTypeEnum.SAVE.getCode());
        //当前事务保存到ThreadLocal
        CURRENT.set(transaction);
        //设置tcc事务上下文，这个类会传递给远端
        TransactionContext context = new TransactionContext();
        //设置事务id
        context.setTransId(transaction.getTransId());
        //设置为发起者角色
        context.setRole(RoleEnum.START.getCode());
        TransactionContextLocal.getInstance().set(context);
    }

    /**
     * save errorMsg into Transaction.
     *
     * @param errorMsg
     */
    public void failTransaction(final String errorMsg) {
        Transaction transaction = getCurrentTransaction();
        if (Objects.nonNull(transaction)) {
            transaction.setStatus(StatusEnum.FAILURE.getCode());
            transaction.setErrorMsg(errorMsg);
            publisher.publishEvent(transaction, EventTypeEnum.UPDATE_FAIR.getCode());
        }
    }

    /**
     * this is acotr begin transaction.
     *
     * @param point              cut point
     * @param transactionContext {@linkplain TransactionContext}
     */
    public void actorTransaction(final ProceedingJoinPoint point, final TransactionContext transactionContext) {
        Transaction transaction = buildTransaction(point, RoleEnum.PROVIDER.getCode(),
                StatusEnum.BEGIN.getCode(), transactionContext.getTransId());
        //发布事件保存事件，异步保存
        publisher.publishEvent(transaction, EventTypeEnum.SAVE.getCode());
        //当前事务保存到ThreadLocal
        CURRENT.set(transaction);
        //设置提供者角色
        transactionContext.setRole(RoleEnum.PROVIDER.getCode());
        TransactionContextLocal.getInstance().set(transactionContext);
    }

    /**
     * update transaction status.
     *
     * @param status {@linkplain StatusEnum}
     */
    public void updateStatus(final int status) {
        Transaction transaction = getCurrentTransaction();
        Optional.ofNullable(transaction)
                .map(t -> {
                    t.setStatus(status);
                    return t;
                }).ifPresent(t -> publisher.publishEvent(t, EventTypeEnum.UPDATE_STATUS.getCode()));
        transaction.setStatus(StatusEnum.COMMIT.getCode());
    }

    /**
     * send message.
     */
    public void sendMessage() {
        Optional.ofNullable(getCurrentTransaction()).ifPresent(sendMessageService::sendMessage);
    }

    /**
     * transaction is begin.
     *
     * @return
     */
    public boolean isBegin() {
        return CURRENT.get() != null;
    }

    /**
     * help gc.
     */
    public void cleanThreadLocal() {
        CURRENT.remove();
    }

    /**
     * add participant into transaction.
     *
     * @param participant {@linkplain Participant}
     */
    public void registerParticipant(final Participant participant) {
        final Transaction transaction = this.getCurrentTransaction();
        transaction.registerParticipant(participant);
        publisher.publishEvent(transaction, EventTypeEnum.UPDATE_PARTICIPANT.getCode());
    }

    private Transaction getCurrentTransaction() {
        return CURRENT.get();
    }

    private Transaction buildTransaction(final ProceedingJoinPoint point, final int role,
                                         final int status, final String transId) {
        Transaction transaction;
        if (StringUtils.isNoneBlank(transId)) {
            transaction = new Transaction(transId);
        } else {
            transaction = new Transaction();
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = point.getTarget().getClass();
        transaction.setStatus(status);
        transaction.setRole(role);
        transaction.setTargetClass(clazz.getName());
        transaction.setTargetMethod(method.getName());
        return transaction;
    }
}
