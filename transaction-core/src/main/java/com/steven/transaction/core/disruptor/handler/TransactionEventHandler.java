package com.steven.transaction.core.disruptor.handler;

import com.lmax.disruptor.WorkHandler;
import com.steven.transaction.common.bean.entity.Transaction;
import com.steven.transaction.common.enums.EventTypeEnum;
import com.steven.transaction.core.coordinator.CoordinatorService;
import com.steven.transaction.core.disruptor.event.TransactionEvent;

import java.util.concurrent.Executor;

/**
 * TransactionEventHandler.
 *
 * @author steven
 */
public class TransactionEventHandler implements WorkHandler<TransactionEvent  > {

    private final CoordinatorService coordinatorService;

    private Executor executor;

    public TransactionEventHandler(final CoordinatorService coordinatorService,final Executor executor) {
        this.coordinatorService = coordinatorService;
        this.executor = executor;
    }

    @Override
    public void onEvent(TransactionEvent transactionEvent) throws Exception {
        executor.execute(()->{
            if (transactionEvent.getType() == EventTypeEnum.SAVE.getCode()) {
                coordinatorService.save(transactionEvent.getTransaction());
            } else if (transactionEvent.getType() == EventTypeEnum.UPDATE_PARTICIPANT.getCode()) {
                coordinatorService.updateParticipant(transactionEvent.getTransaction());
            } else if (transactionEvent.getType() == EventTypeEnum.UPDATE_STATUS.getCode()) {
                final Transaction transaction = transactionEvent.getTransaction();
                coordinatorService.updateStatus(transaction.getTransId(), transaction.getStatus());
            } else if (transactionEvent.getType() == EventTypeEnum.UPDATE_FAIR.getCode()) {
                coordinatorService.updateFailTransaction(transactionEvent.getTransaction());
            }
            transactionEvent.clear();
        });
    }
}
