package com.steven.transaction.core.spi;

import com.steven.transaction.common.bean.entity.Transaction;
import com.steven.transaction.common.config.TransactionConfig;
import com.steven.transaction.common.exception.TransactionRuntimeException;
import com.steven.transaction.common.serializer.ObjectSerializer;

import java.util.Date;
import java.util.List;

/**
 * CoordinatorRepository.
 *
 * @author steven
 */
public interface CoordinatorRepository {

    /**
     * create transaction.
     *
     * @param transaction {@linkplain Transaction}
     * @return influence row number
     */
    int create(Transaction transaction);

    /**
     * delete transaction.
     *
     * @param transId pk
     * @return influence row number
     */
    int remove(String transId);

    /**
     * update transaction.{@linkplain Transaction}
     *
     * @param transaction 事务对象
     * @return influence row number
     * @throws TransactionRuntimeException ex {@linkplain TransactionRuntimeException}
     */
    int update(Transaction transaction) throws TransactionRuntimeException;

    /**
     * update fail info in transaction.
     *
     * @param transaction {@linkplain Transaction}
     * @throws TransactionRuntimeException ex {@linkplain TransactionRuntimeException}
     */
    void updateFailTransaction(Transaction transaction) throws TransactionRuntimeException;

    /**
     * update participants in transaction.
     * this have only update this participant filed.
     *
     * @param transaction {@linkplain Transaction}
     * @throws TransactionRuntimeException ex {@linkplain TransactionRuntimeException}
     */
    void updateParticipant(Transaction transaction) throws TransactionRuntimeException;

    /**
     * update status in transaction.
     *
     * @param transId pk
     * @param status  {@linkplain com.steven.transaction.common.enums.StatusEnum}
     * @return influence row number
     * @throws TransactionRuntimeException ex {@linkplain TransactionRuntimeException}
     */
    int updateStatus(String transId, Integer status) throws TransactionRuntimeException;

    /**
     * find transaction by transId.
     *
     * @param transId pk
     * @return {@linkplain Transaction}
     */
    Transaction findByTransId(String transId);

    /**
     * list all transaction by delay date.
     *
     * @param date delay date
     * @return list transaction
     */
    List<Transaction> listAllbyDelay(Date date);

    /**
     * init CoordinatorRepository.
     *
     * @param modelName         model name
     * @param transactionConfig {@linkplain TransactionConfig}
     * @throws TransactionRuntimeException ex {@linkplain TransactionRuntimeException}
     */
    void init(String modelName, TransactionConfig transactionConfig) throws TransactionRuntimeException;

    /**
     * get scheme.
     *
     * @return scheme
     */
    String getScheme();

    /**
     *set objectserializer.
     *
     * @param objectSerializer
     */
    void setSerializer(ObjectSerializer objectSerializer);
}
