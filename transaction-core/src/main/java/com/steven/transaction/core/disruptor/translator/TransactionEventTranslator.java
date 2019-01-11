package com.steven.transaction.core.disruptor.translator;


import com.lmax.disruptor.EventTranslatorOneArg;
import com.steven.transaction.common.bean.entity.Transaction;
import com.steven.transaction.core.disruptor.event.TransactionEvent;

/**
 * TransactionEventTranslator.
 *
 * @author steven
 */
public class TransactionEventTranslator implements EventTranslatorOneArg<TransactionEvent, Transaction> {

    private int type;

    public TransactionEventTranslator(final int type) {
        this.type = type;
    }

    @Override
    public void translateTo(final TransactionEvent transactionEvent,final long l,final Transaction transaction) {
        transactionEvent.setTransaction(transaction);
        transactionEvent.setType(type);
    }
}
