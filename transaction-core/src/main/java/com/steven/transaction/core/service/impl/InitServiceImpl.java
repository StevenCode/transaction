package com.steven.transaction.core.service.impl;

import com.steven.transaction.common.config.TransactionConfig;
import com.steven.transaction.core.concurrent.coordinator.CoordinatorService;
import com.steven.transaction.core.disruptor.publisher.TransactionEventPublisher;
import com.steven.transaction.core.schedule.ScheduledService;
import com.steven.transaction.core.service.InitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * InitServiceImpl.
 *
 * @author steven
 */
public class InitServiceImpl implements InitService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InitServiceImpl.class);

    private final CoordinatorService coordinatorService;

    private final TransactionEventPublisher publisher;

    private final ScheduledService scheduledService;

    @Autowired
    public InitServiceImpl(final CoordinatorService coordinatorService, final TransactionEventPublisher publisher,final ScheduledService scheduledService) {
        this.coordinatorService = coordinatorService;
        this.publisher = publisher;
        this.scheduledService = scheduledService;
    }

    @Override
    public void initialization(TransactionConfig transactionConfig) {

    }
}
