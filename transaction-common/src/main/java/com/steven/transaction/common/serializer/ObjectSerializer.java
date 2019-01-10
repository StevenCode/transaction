package com.steven.transaction.common.serializer;

import com.steven.transaction.common.exception.TransactionException;

/**
 * ObjectSerializer.
 *
 * @author steven
 */
public interface ObjectSerializer {

    /**
     * 序列化对象.
     *
     * @param obj 需要序列化的对象
     * @return byte[]
     * @throws TransactionException 异常信息
     */
    byte[] serialize(Object obj) throws TransactionException;

    /**
     * 反序列化对象.
     *
     * @param param 需要反序列化的byte[]
     * @param clazz java对象
     * @param <T> 泛型支持
     * @return 对象
     * @throws TransactionException 异常信息
     */
    <T> T deSerializer(byte[] param, Class<T> clazz) throws TransactionException;

    /**
     * 设置scheme.
     *
     * @return scheme 命名
     */
    String getScheme();
}
