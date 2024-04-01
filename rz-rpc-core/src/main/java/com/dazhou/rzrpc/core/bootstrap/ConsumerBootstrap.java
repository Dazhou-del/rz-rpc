package com.dazhou.rzrpc.core.bootstrap;

import com.dazhou.rzrpc.core.RpcApplication;

/**
 * 服务消费者启动类（初始化）
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-01 20:32
 */
public class ConsumerBootstrap {

    /**
     * 初始化
     */
    public static void init() {
        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();
    }
}
