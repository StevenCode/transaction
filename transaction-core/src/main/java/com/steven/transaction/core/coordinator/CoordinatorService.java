package com.steven.transaction.core.coordinator;

import com.steven.transaction.common.bean.entity.Transaction;
import com.steven.transaction.common.config.TransactionConfig;
import com.steven.transaction.common.exception.TransactionException;
import com.steven.transaction.common.exception.TransactionRuntimeException;
import com.steven.transaction.common.serializer.ObjectSerializer;

import java.util.Date;
import java.util.List;

/**
 * CoordinatorService.
 *
 * @author steven
 */
public interface CoordinatorService {

    /**
     * start coordinator service.
     *
     * @param transactionConfig {@linkplain TransactionConfig}
     * @throws TransactionException ex
     */
    void start(TransactionConfig transactionConfig) throws TransactionException;

    /**
     * save transaction.
     *
     * @param transaction {@linkplain Transaction}
     * @return pk
     */
    String save(Transaction transaction);

    /**
     * find transaction by id.
     *
     * @param transId pk
     * @return {@linkplain Transaction}
     */
    Transaction findByTransId(String transId);

    /**
     * find transaction by Delay Date.
     *
     * @param date delay date
     * @return {@linkplain Transaction}
     */
    List<Transaction> listAllByDelay(Date date);

    /**
     * delete transaction.
     *
     * @param transId pk
     * @return true false
     */
    boolean remove(String transId);

    /**
     * update transaction.
     *
     * @param transaction {@linkplain Transaction}
     * @return rows 1
     * @throws TransactionRuntimeException ex
     */
    int update(Transaction transaction) throws TransactionRuntimeException;

    /**
     * update fail info.
     *
     * @param transaction {@linkplain Transaction}
     * @throws TransactionRuntimeException ex
     */
    void updateFailTransaction(Transaction transaction) throws TransactionRuntimeException;

    /**
     *update participant.
     *
     * @param transaction {@linkplain Transaction}
     * @throws TransactionRuntimeException ex
     */
    void updateParticipant(Transaction transaction) throws TransactionRuntimeException;

    /**
     * update status.
     * @param transId pk
     * @param status status
     * @return rows 1
     * @throws TransactionRuntimeException ex
     */
    int updateStatus(String transId, Integer status) throws TransactionRuntimeException;

    /**
     * set ObjectSerializer.
     * @param objectSerializer
     */
    void setSerializer(ObjectSerializer objectSerializer);
}
