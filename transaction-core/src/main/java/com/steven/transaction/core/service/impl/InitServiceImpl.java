package com.steven.transaction.core.service.impl;

import com.steven.transaction.common.config.TransactionConfig;
import com.steven.transaction.common.enums.RepositorySupportEnum;
import com.steven.transaction.common.enums.SerializeEnum;
import com.steven.transaction.common.serializer.KryoSerializer;
import com.steven.transaction.common.serializer.ObjectSerializer;
import com.steven.transaction.common.utils.LogUtil;
import com.steven.transaction.common.utils.ServiceBootstrap;
import com.steven.transaction.core.concurrent.coordinator.CoordinatorService;
import com.steven.transaction.core.disruptor.publisher.TransactionEventPublisher;
import com.steven.transaction.core.helper.SpringBeanUtils;
import com.steven.transaction.core.schedule.ScheduledService;
import com.steven.transaction.core.service.InitService;
import com.steven.transaction.core.spi.CoordinatorRepository;
import com.steven.transaction.core.spi.repository.JdbcCoordinatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> LOGGER.error("transaction have error")));
        try {
            loadSpiSupport(transactionConfig);
            publisher.start(transactionConfig.getBufferSize());
            coordinatorService.start(transactionConfig);
            //如果需要自动恢复 开启线程 调度线程池，进行恢复
            if (transactionConfig.getNeedRecover()) {
                scheduledService.scheduledAutoRecover(transactionConfig);
            }
        } catch (Exception e) {
            LogUtil.error(LOGGER, "transaction init fail:{}", e::getMessage);
            //非正常关闭
            System.exit(1);
        }
        LogUtil.info(LOGGER, () -> "transaction init success");
    }

    private void loadSpiSupport(final TransactionConfig transactionConfig) {
        //spi serialize
        final SerializeEnum serializeEnum = SerializeEnum.acquire(transactionConfig.getSerializer());
        final ServiceLoader<ObjectSerializer> objectSerializers = ServiceBootstrap.loadAll(ObjectSerializer.class);
        final ObjectSerializer serializer =
                StreamSupport.stream(objectSerializers.spliterator(),
                        true)
                        .filter(objectSerializer -> Objects.equals(objectSerializer.getScheme(), serializeEnum.getSerialize()))
                        .findFirst()
                        .orElse(new KryoSerializer());
        coordinatorService.setSerializer(serializer);
        SpringBeanUtils.getInstance().registerBean(ObjectSerializer.class.getName(), serializer);
        //spi repository support
        final RepositorySupportEnum repositorySupportEnum = RepositorySupportEnum.acquire(transactionConfig.getRepositorySupport());
        final ServiceLoader<CoordinatorRepository> recoverRepositories = ServiceBootstrap.loadAll(CoordinatorRepository.class);
        final CoordinatorRepository repository =
                StreamSupport.stream(recoverRepositories.spliterator(), false)
                        .filter(recoverRepository -> Objects.equals(recoverRepository.getScheme(), repositorySupportEnum.getSupport()))
                        .findFirst()
                        .orElse(new JdbcCoordinatorRepository());
        //将CoordinatorRepository实现注入到spring容器
        repository.setSerializer(serializer);
        SpringBeanUtils.getInstance().registerBean(CoordinatorRepository.class.getName(), repository);
    }
}
