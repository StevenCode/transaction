package com.steven.transaction.core.coordinator.impl;

import com.steven.transaction.common.bean.entity.Transaction;
import com.steven.transaction.common.config.TransactionConfig;
import com.steven.transaction.common.exception.TransactionException;
import com.steven.transaction.common.exception.TransactionRuntimeException;
import com.steven.transaction.common.serializer.ObjectSerializer;
import com.steven.transaction.core.coordinator.CoordinatorService;
import com.steven.transaction.core.helper.SpringBeanUtils;
import com.steven.transaction.core.service.RpcApplicationService;
import com.steven.transaction.core.spi.CoordinatorRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * CoordinatorServiceImpl.
 *
 * @author steven
 */
public class CoordinatorServiceImpl implements CoordinatorService {

    private CoordinatorRepository coordinatorRepository;

    private final RpcApplicationService rpcApplicationService;

    @Autowired
    public CoordinatorServiceImpl(final RpcApplicationService rpcApplicationService) {
        this.rpcApplicationService = rpcApplicationService;
    }

    @Override
    public void start(TransactionConfig transactionConfig) throws TransactionException {
        coordinatorRepository = SpringBeanUtils.getInstance().getBean(CoordinatorRepository.class);
        final String repositorySuffix = buildRepositorySuffix(transactionConfig.getRepositorySuffix());
        //初始化spi 协调资源存储
        coordinatorRepository.init(repositorySuffix, transactionConfig);
    }

    @Override
    public String save(Transaction transaction) {
        final int rows = coordinatorRepository.create(transaction);
        if (rows > 0) {
            return transaction.getTransId();
        }
        return null;
    }

    @Override
    public Transaction findByTransId(String transId) {
        return coordinatorRepository.findByTransId(transId);
    }

    @Override
    public List<Transaction> listAllByDelay(Date date) {
        return coordinatorRepository.listAllbyDelay(date);
    }

    @Override
    public boolean remove(String transId) {
        return  coordinatorRepository.remove(transId) > 0;
    }

    @Override
    public int update(Transaction transaction) throws TransactionRuntimeException {
        return coordinatorRepository.update(transaction);
    }

    @Override
    public void updateFailTransaction(Transaction transaction) throws TransactionRuntimeException {
        coordinatorRepository.updateFailTransaction(transaction);
    }

    @Override
    public void updateParticipant(Transaction transaction) throws TransactionRuntimeException {
        coordinatorRepository.updateParticipant(transaction);
    }

    @Override
    public int updateStatus(String transId, Integer status) throws TransactionRuntimeException {
        return coordinatorRepository.updateStatus(transId, status);
    }

    @Override
    public void setSerializer(ObjectSerializer objectSerializer) {

    }

    private String buildRepositorySuffix(final String repositorySuffix) {
        if (StringUtils.isNoneBlank(repositorySuffix)) {
            return repositorySuffix;
        }else {
            return rpcApplicationService.acquireName();
        }
    }
}
