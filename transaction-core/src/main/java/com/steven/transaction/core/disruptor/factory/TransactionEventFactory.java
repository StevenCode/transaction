package com.steven.transaction.core.disruptor.factory;

import com.lmax.disruptor.EventFactory;
import com.steven.transaction.core.disruptor.event.TransactionEvent;

/**
 * TransactionEventFactory.
 *
 * @author steven
 */
public class TransactionEventFactory implements  EventFactory<TransactionEvent>{
    @Override
    public TransactionEvent newInstance() {
        return new TransactionEvent();
    }
}
