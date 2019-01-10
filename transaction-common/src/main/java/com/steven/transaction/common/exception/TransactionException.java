package com.steven.transaction.common.exception;

/**
 * TransactionException.
 *
 * @author steven
 */
public class TransactionException extends Exception {
    private static final long serialVersionUID = -948934144333391208L;

    public TransactionException() {
    }

    public TransactionException(final String message) {
        super(message);
    }

    public TransactionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TransactionException(final Throwable cause) {
        super(cause);
    }
}
