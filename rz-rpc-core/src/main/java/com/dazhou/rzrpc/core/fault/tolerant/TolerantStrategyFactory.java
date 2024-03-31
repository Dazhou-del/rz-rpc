package com.dazhou.rzrpc.core.fault.tolerant;

import com.dazhou.rzrpc.core.serializer.SpiLoader;

/**
 * 容错策略工厂（工厂模式，用于获取容错策略对象）
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-31 20:56
 */
public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    //默认容错策略
    private static final TolerantStrategy DEFAULT_RETRY_STRATEGY=new FailBackTolerantStrategy();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }
}
