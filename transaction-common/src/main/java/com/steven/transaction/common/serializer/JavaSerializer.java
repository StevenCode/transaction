package com.steven.transaction.common.serializer;

import com.steven.transaction.common.enums.SerializeEnum;
import com.steven.transaction.common.exception.TransactionException;

import java.io.*;

/**
 * JavaSerializer.
 *
 * @author steven
 */
public class JavaSerializer implements ObjectSerializer{
    @Override
    public byte[] serialize(Object obj) throws TransactionException {

        try(ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutput objectOutput = new ObjectOutputStream(arrayOutputStream)){
            objectOutput.writeObject(obj);
            objectOutput.flush();
            return arrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new TransactionException("java serialize error " + e.getMessage());
        }
    }

    @Override
    public <T> T deSerializer(byte[] param, Class<T> clazz) throws TransactionException {
        try (ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(param); ObjectInput input = new ObjectInputStream(arrayInputStream)) {
            return (T) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new TransactionException("java deSerialize error " + e.getMessage());
        }
    }

    /**
     * 设置scheme.
     *
     * @return scheme 命名
     */
    @Override
    public String getScheme() {
        return SerializeEnum.JDK.getSerialize();
    }
}
