package com.steven.transaction.core.disruptor.publisher;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.steven.transaction.common.bean.entity.Transaction;
import com.steven.transaction.core.coordinator.CoordinatorService;
import com.steven.transaction.core.concurrent.threadpool.TransactionThreadFactory;
import com.steven.transaction.core.disruptor.event.TransactionEvent;
import com.steven.transaction.core.disruptor.factory.TransactionEventFactory;
import com.steven.transaction.core.disruptor.handler.TransactionEventHandler;
import com.steven.transaction.core.disruptor.translator.TransactionEventTranslator;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TransactionEventPublisher.
 *
 * @author steven
 */
@Component
public class TransactionEventPublisher implements DisposableBean {

    private static final int MAX_THREAD = Runtime.getRuntime().availableProcessors() << 1;

    private Disruptor<TransactionEvent> disruptor;

    private final CoordinatorService coordinatorService;

    @Autowired
    public TransactionEventPublisher(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    /**
     * start disruptor
     *
     * @param bufferSize bufferSize
     */
    public void start(final int bufferSize) {
        disruptor = new Disruptor<TransactionEvent>(new TransactionEventFactory(), bufferSize, r -> {
            AtomicInteger index = new AtomicInteger(1);
            return new Thread(null, r, "disruptor-thread-" + index.getAndIncrement());
        }, ProducerType.MULTI, new BlockingWaitStrategy());

        final Executor executor = new ThreadPoolExecutor(MAX_THREAD, MAX_THREAD, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                TransactionThreadFactory.create("log-disruptor", false),
                new ThreadPoolExecutor.AbortPolicy());

        TransactionEventHandler[] consumers = new TransactionEventHandler[MAX_THREAD];
        for (int i = 0; i < MAX_THREAD; i++) {
            consumers[i] = new TransactionEventHandler(coordinatorService, executor);
        }
        disruptor.handleEventsWithWorkerPool(consumers);
        disruptor.setDefaultExceptionHandler(new IgnoreExceptionHandler());
        disruptor.start();
    }

    public void publishEvent(final Transaction transaction, final int type) {
        RingBuffer<TransactionEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent(new TransactionEventTranslator(type), transaction);
    }

    @Override
    public void destroy() throws Exception {
        disruptor.shutdown();
    }
}
