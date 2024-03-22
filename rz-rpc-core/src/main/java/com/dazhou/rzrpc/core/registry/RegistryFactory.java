package com.dazhou.rzrpc.core.registry;

import com.dazhou.rzrpc.core.serializer.SpiLoader;

/**
 * 注册中心工厂（用于获取注册中心对象）
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-21 17:38
 */
public class RegistryFactory {

    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class,key);
    }
}
