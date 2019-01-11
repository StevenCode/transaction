package com.steven.transaction.common.enums;

/**
 * EventTypeEnum.
 *
 * @author steven
 */
public enum EventTypeEnum {

    /**
     * Save coordinator action enum.
     */
    SAVE(0,"保存"),

    /**
     * Delete coordinator action enum.
     */
    DELETE(1,"删除"),

    /**
     * Update coordinator action enum.
     */
    UPDATE_STATUS(2, "更新状态"),

    /**
     * Update participant event type enum.
     */
    UPDATE_PARTICIPANT(3,"更新参与者"),

    /**
     * Update fair event type enum.
     */
    UPDATE_FAIR(4, "更新错误信息");

    private int code;

    private String desc;

    EventTypeEnum(final int code, final String desc) {
        this.code = code;
        this.desc = desc;
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
