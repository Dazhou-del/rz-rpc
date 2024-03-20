package com.dazhou.example.provider;

import com.dazhou.example.common.service.UserService;
import com.dazhou.rzrpc.core.RpcApplication;
import com.dazhou.rzrpc.core.config.RpcConfig;
import com.dazhou.rzrpc.core.registry.LocalRegistry;
import com.dazhou.rzrpc.core.service.VertxHttpServer;

import com.dazhou.rzrpc.service.HttpServer;

/**
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 12:00
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        //RPC框架初始化
        RpcApplication.init();


        //将服务注册到本地注册器中
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
