package com.steven.transaction.common.enums;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * SerializeEnum.
 *
 * @author steven
 */
public enum  SerializeEnum {

    /**
     * Jdk serialize protocol enum.
     */
    JDK("jdk"),

    /**
     * Kryo serialize protocol enum.
     */
    KRYO("kryo"),

    /**
     * Hessian serialize protocol enum.
     */
    HESSIAN("hessian"),

    /**
     * Protostuff serialize protocol enum.
     */
    PROTOSTUFF("protostuff");

    private String serialize;

    SerializeEnum(final String serialize) {
        this.serialize = serialize;
    }

    /**
     * Acquire serialize protocol serialize protocol enum.
     */
    public static SerializeEnum acquire(final String serialize) {
        Optional<SerializeEnum> serializeEnum =
                Arrays.stream(SerializeEnum.values())
                        .filter(v -> Objects.equals(v.getSerialize(), serialize))
                        .findFirst();
        return serializeEnum.orElse(SerializeEnum.KRYO);
    }


    /**
     * Gets serialize protocol.
     *
     * @return the serialize protocol
     */
    public String getSerialize() {
        return serialize;
    }

    /**
     * Sets serialize protocol.
     *
     * @param serialize the serialize protocol
     */
    public void setSerialize(final String serialize) {
        this.serialize = serialize;
    }
}
