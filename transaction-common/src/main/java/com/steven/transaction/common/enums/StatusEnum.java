package com.steven.transaction.common.enums;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * StatusEnum.
 *
 * @author steven
 */
public enum  StatusEnum {

    /**
     * Rollback transaction status enum.
     */
    ROLLBACK(0, "回滚"),

    /**
     * Commint transaction status enum.
     */
    COMMIT(1, "已提交"),

    /**
     * Begin transaction status enum.
     */
    BEGIN(2, "开始"),

    /**
     * Running transaction status enum.
     */
    SEND_MSG(3, "可以发送消息"),

    /**
     * Failure transaction status enum.
     */
    FAILURE(3, "预提交"),

    /**
     * Lock transaction status enum.
     */
    LOCK(6, "锁定");

    private int code;

    private String desc;

    StatusEnum(final int code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static StatusEnum acquireByCode(final int code) {
        Optional<StatusEnum> transactionStatusEnum =
                Arrays.stream(StatusEnum.values())
                        .filter(v -> Objects.equals(v.getCode(), code))
                        .findFirst();
        return transactionStatusEnum.orElse(StatusEnum.BEGIN);
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(final int code) {
        this.code = code;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets desc.
     *
     * @param desc the desc
     */
    public void setDesc(final String desc) {
        this.desc = desc;
    }
}
