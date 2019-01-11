package com.steven.transaction.core.concurrent.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TransactionThreadFactory.
 *
 * @author steven
 */
public final class TransactionThreadFactory implements ThreadFactory {

    private static volatile boolean daemon;

    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("Transaction");

    private final AtomicLong threadNumber = new AtomicLong(1);

    private final String namePrefix;

    private TransactionThreadFactory(final String namePrefix, final boolean daemon) {
        this.namePrefix = namePrefix;
        TransactionThreadFactory.daemon = daemon;
    }

    /**
     * create ThreadFactory.
     *
     * @param namePrefix namePrefix
     * @param daemon daemon
     * @return ThreadFactory
     */
    public static ThreadFactory create(final String namePrefix, final boolean daemon) {
        return new TransactionThreadFactory(namePrefix, daemon);
    }
    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(THREAD_GROUP, runnable,
                THREAD_GROUP.getName() + "-" + namePrefix + "-" + threadNumber.getAndIncrement());
        thread.setDaemon(daemon);
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
