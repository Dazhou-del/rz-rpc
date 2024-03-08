package com.dazhou.rzrpc.core.serializer;

import java.io.IOException;

/**
 * 序列化接口
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 14:21
 */
public interface Serializer {
    /**
     * 序列化
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T object) throws IOException;

    /**
     * 反序列化
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}
