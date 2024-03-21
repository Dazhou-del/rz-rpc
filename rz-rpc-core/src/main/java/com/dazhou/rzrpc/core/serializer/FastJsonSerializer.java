package com.dazhou.rzrpc.core.serializer;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONB;

import java.io.IOException;

/**
 * fastjson序列化器
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-20 15:54
 */
public class FastJsonSerializer implements Serializer{
    @Override
    public <T> byte[] serialize(T t) {

        return JSONB.toBytes(t);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSONB.parseObject(data,clazz);
    }




}
