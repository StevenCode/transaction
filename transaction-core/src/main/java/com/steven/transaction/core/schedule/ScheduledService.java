package com.steven.transaction.core.schedule;

import com.steven.transaction.common.bean.entity.Transaction;
import com.steven.transaction.common.config.TransactionConfig;
import com.steven.transaction.common.enums.EventTypeEnum;
import com.steven.transaction.common.enums.StatusEnum;
import com.steven.transaction.common.utils.LogUtil;
import com.steven.transaction.core.coordinator.CoordinatorService;
import com.steven.transaction.core.concurrent.threadpool.TransactionThreadFactory;
import com.steven.transaction.core.disruptor.publisher.TransactionEventPublisher;
import com.steven.transaction.core.service.SendMessageService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledService.
 *
 * @author steven
 */
@Component
public class ScheduledService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledService.class);

    @Autowired
    private SendMessageService sendMessageService;

    @Autowired
    private CoordinatorService coordinatorService;

    @Autowired
    private TransactionEventPublisher publisher;

    public void scheduledAutoRecover(final TransactionConfig transactionConfig) {
        new ScheduledThreadPoolExecutor(1, TransactionThreadFactory.create("AutoRecoverService", true))
                .scheduleWithFixedDelay(() -> {
                    LogUtil.debug(LOGGER, "auto recover execute delay Time:{}", transactionConfig::getScheduledDelay);
                    try {
                        List<Transaction> transactionList = coordinatorService.listAllByDelay(acquireDate(transactionConfig));
                        if (CollectionUtils.isNotEmpty(transactionList)) {
                            transactionList.forEach(transaction -> {
                                final Boolean success = sendMessageService.sendMessage(transaction);
                                //发送成功,更改状态
                                if (success) {
                                    transaction.setStatus(StatusEnum.COMMIT.getCode());
                                    publisher.publishEvent(transaction, EventTypeEnum.UPDATE_STATUS.getCode());
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 30, transactionConfig.getScheduledDelay(), TimeUnit.SECONDS);
    }

    private Date acquireDate(final TransactionConfig transactionConfig) {
        return new Date(LocalDateTime.now().atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli() - transactionConfig.getRecoverDelayTime() * 1000);
    }
}
