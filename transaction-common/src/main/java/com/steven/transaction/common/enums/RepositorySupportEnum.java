package com.steven.transaction.common.enums;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * RepositorySupportEnum.
 *
 * @author steven
 */
public enum  RepositorySupportEnum {

    /**
     * Db compensate cache type enum.
     */
    DB("db"),

    /**
     * File compensate cache type enum.
     */
    FILE("file"),

    /**
     * Redis compensate cache type enum.
     */
    REDIS("redis"),

    /**
     * Mongodb compensate cache type enum.
     */
    MONGODB("mongodb"),

    /**
     * Zookeeper compensate cache type enum.
     */
    ZOOKEEPER("zookeeper");

    private String support;

    RepositorySupportEnum(final String support) {
        this.support = support;
    }

    public static RepositorySupportEnum acquire(final String support) {
        Optional<RepositorySupportEnum> repositorySupportEnum =
                Arrays.stream(RepositorySupportEnum.values())
                        .filter(v -> Objects.equals(v.getSupport(), support))
                        .findFirst();

        return repositorySupportEnum.orElse(RepositorySupportEnum.DB);
    }

    /**
     * Gets support.
     *
     * @return the support
     */
    public String getSupport() {
        return support;
    }

    /**
     * Sets support.
     *
     * @param support the support
     */
    public void setSupport(final String support) {
        this.support = support;
    }
}
