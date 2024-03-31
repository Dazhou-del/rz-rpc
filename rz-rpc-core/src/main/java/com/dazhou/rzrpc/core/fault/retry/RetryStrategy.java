package com.dazhou.rzrpc.core.fault.retry;

import com.dazhou.rzrpc.core.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-31 16:36
 */
public interface RetryStrategy {

    /**
     * 重试方法
     *
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
