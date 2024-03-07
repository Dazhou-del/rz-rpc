package com.dazhou.example.provider;

import com.dazhou.example.common.service.UserService;
import com.dazhou.rzrpc.registry.LocalRegistry;
import com.dazhou.rzrpc.service.HttpServer;
import com.dazhou.rzrpc.service.VertxHttpServer;

/**
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 12:00
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        //将服务注册到本地注册器中
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
