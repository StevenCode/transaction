package com.steven.transaction.core.disruptor.event;

import com.steven.transaction.common.bean.entity.Transaction;
import lombok.Data;

import java.io.Serializable;

/**
 * TransactionEvent.
 *
 * @author steven
 */
@Data
public class TransactionEvent implements Serializable {

    private Transaction transaction;

    private int type;

    /**
     * help gc.
     */
    public void clear() {
        transaction = null;
    }
}
