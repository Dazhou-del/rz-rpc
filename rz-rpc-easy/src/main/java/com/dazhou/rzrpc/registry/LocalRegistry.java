package com.dazhou.rzrpc.registry;

import cn.hutool.core.map.CustomKeyMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册器
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 14:08
 */
public class LocalRegistry {

    /**
     * 注册信息存储
     */
    private static final Map<String,Class<?>> map=new ConcurrentHashMap<>();


    /**
     * 服务注册
     * @param serviceName
     * @param clazz
     */
    public static  void  register(String serviceName, Class<?> clazz){
        map.put(serviceName,clazz);
    }


    /**
     * 获取服务
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName){
        return map.get(serviceName);
    }

    /**
     * 删除服务
     * @param serviceName
     */
    public static void delete(String serviceName){
        map.remove(serviceName);
    }
}
