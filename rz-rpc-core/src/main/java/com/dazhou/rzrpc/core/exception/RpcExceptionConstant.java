package com.dazhou.rzrpc.core.exception;

/**
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-05 20:41
 */
public interface RpcExceptionConstant {
    /**
     * 消费者异常
     */
    int consumerErrorCode = 600;

    /**
     * 注册中心异常
     */
    int registryErrorCode = 700;
    /**
     * 提供者异常
     */
    int providerErrorCode = 800;

    /**
     * rpc框架异常
     */
    int rpcErrorCode = 900;

}
