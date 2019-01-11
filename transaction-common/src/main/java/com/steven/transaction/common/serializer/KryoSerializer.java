package com.steven.transaction.common.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.steven.transaction.common.enums.SerializeEnum;
import com.steven.transaction.common.exception.TransactionException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * KryoSerializer.
 *
 * @author steven
 */
public class KryoSerializer implements ObjectSerializer {

    /**
     * 序列化.
     *
     * @param obj 需要序列化的对象
     * @return 序列化后的byte 数组
     * @throws TransactionException 异常
     */
    @Override
    public byte[] serialize(Object obj) throws TransactionException {
        byte[] bytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); Output output = new Output(outputStream)) {
            Kryo kryo = new Kryo();
            kryo.writeObject(output, obj);
            bytes = output.toBytes();
            output.flush();
        } catch (IOException ex) {
            throw new TransactionException("kryo serialize error " + ex.getMessage());
        }
        return bytes;
    }

    /**
     * 反序列化.
     *
     * @param param 需要反序列化的byte[]
     * @param clazz java对象
     * @return 序列化对象
     * @throws TransactionException
     */
    @Override
    public <T> T deSerialize(byte[] param, Class<T> clazz) throws TransactionException {
        T object;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(param)) {
            Kryo kryo = new Kryo();
            Input input = new Input(inputStream);
            object = kryo.readObject(input, clazz);
            input.close();
        } catch (IOException e) {
            throw new TransactionException("kryo deSerialize error" + e.getMessage());
        }
        return object;
    }

    /**
     * 设置 scheme.
     *
     * @return scheme命名
     */
    @Override
    public String getScheme() {
        return SerializeEnum.KRYO.getSerialize();
    }
}
