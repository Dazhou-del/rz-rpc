package com.dazhou.rzrpc.service;

/**
 * HTTP 服务器接口
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 11:16
 */
public interface HttpServer {
    /**
     * 启动服务器
     * @param port
     */
    void doStart(int port);
}
