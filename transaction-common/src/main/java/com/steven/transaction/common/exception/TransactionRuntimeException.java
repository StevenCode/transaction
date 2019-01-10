package com.steven.transaction.common.exception;

/**
 * TransactionRuntimeException.
 *
 * @author steven
 */
public class TransactionRuntimeException extends RuntimeException{
    private static final long serialVersionUID = -1949770547060521702L;

    public TransactionRuntimeException(){

    }

    public TransactionRuntimeException(final String message) {
        super(message);
    }

    public TransactionRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TransactionRuntimeException(final Throwable cause) {
        super(cause);
    }
}
