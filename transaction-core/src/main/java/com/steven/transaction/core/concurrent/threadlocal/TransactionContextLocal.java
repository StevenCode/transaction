package com.steven.transaction.core.concurrent.threadlocal;

import com.steven.transaction.common.context.TransactionContext;

/**
 * TransactionContextLocal.
 *
 * @author steven
 */
public final class TransactionContextLocal {

    private static final ThreadLocal<TransactionContext> CURRENT_LOCAL = new ThreadLocal<>();

    private static final TransactionContextLocal TRANSACTION_CONTEXT_LOCAL = new TransactionContextLocal();

    private TransactionContextLocal() {
    }

    /**
     * Gets instance.
     * @return instance
     */
    public static TransactionContextLocal getInstance() {
        return TRANSACTION_CONTEXT_LOCAL;
    }

    /**
     * Set.
     *
     * @param context the context
     */
    public void set(final TransactionContext context) {
        CURRENT_LOCAL.set(context);
    }

    /**
     * Get myth transaction context.
     *
     * @return the myth transaction context
     */
    public TransactionContext get() {
        return CURRENT_LOCAL.get();
    }

    /**
     * Remove.
     */
    public void remove() {
        CURRENT_LOCAL.remove();
    }
}
