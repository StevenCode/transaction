package com.steven.transaction.common.utils;

import com.steven.transaction.common.exception.TransactionRuntimeException;

/**
 * AssertUtils.
 *
 * @author steven
 */
public final class AssertUtils {
    private AssertUtils() {

    }

    public static void notNull(final Object obj) {
        if (obj == null) {
            throw new TransactionRuntimeException("argument invalid,Please check");
        }
    }
}
