package com.steven.transaction.common.utils;

import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * LogUtil.
 *
 * @author steven
 */
public final class LogUtil {

    private static final LogUtil LOG_UTIL = new LogUtil();

    private LogUtil() {
    }

    public static LogUtil getInstance() {
        return LOG_UTIL;
    }

    public static void debug(Logger logger,String format, Supplier<Object> suppiler) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, suppiler.get());
        }
    }

    public static void debug(Logger logger, Supplier<Object> supplier) {
        if (logger.isDebugEnabled()) {
            logger.debug(Objects.toString(supplier.get()));
        }
    }

    public static void info(Logger logger, String format, Supplier<Object> supplier) {
        if (logger.isInfoEnabled()) {
            logger.info(format, supplier.get());
        }
    }

    public static void info(Logger logger, Supplier<Object> supplier) {
        if (logger.isInfoEnabled()) {
            logger.info(Objects.toString(supplier.get()));
        }
    }

    public static void error(Logger logger, String format, Supplier<Object> supplier) {
        if (logger.isErrorEnabled()) {
            logger.error(format, supplier.get());
        }
    }

    public static void error(Logger logger, Supplier<Object> supplier) {
        if (logger.isErrorEnabled()) {
            logger.error(Objects.toString(supplier.get()));
        }
    }

    public static void warn(Logger logger, String format, Supplier<Object> supplier) {
        if (logger.isWarnEnabled()) {
            logger.warn(format, supplier.get());
        }
    }

    public static void warn(Logger logger, Supplier<Object> supplier) {
        if (logger.isWarnEnabled()) {
            logger.warn(Objects.toString(supplier.get()));
        }
    }
}
