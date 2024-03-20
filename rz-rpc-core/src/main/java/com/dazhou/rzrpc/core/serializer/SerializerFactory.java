package com.dazhou.rzrpc.core.serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化工厂
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-19 16:15
 */
public class SerializerFactory {


    //初始化 各种序列化器 单例模式
/*    private static final Map<String ,Serializer> KEY_SERIALIZER_MAP=new HashMap<String,Serializer>(){{
        put(SerializerKeys.JDK, new JdkSerializer());
        put(SerializerKeys.JSON, new JsonSerializer());
        put(SerializerKeys.KRYO, new KryoSerializer());
        put(SerializerKeys.HESSIAN, new HessianSerializer());
    }};*/


    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
//    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get("jdk");
    private  static final  Serializer DEFAULT_SERIALIZER=new JdkSerializer();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static  Serializer getInstance(String key) {
//        return KEY_SERIALIZER_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
        return SpiLoader.getInstance(Serializer.class,key);
    }

}
