package com.dazhou.rzrpc.core.fault.tolerant;

import com.dazhou.rzrpc.core.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-31 20:43
 */
public interface TolerantStrategy {

    RpcResponse doTolerant(Map<String, Object> context, Exception e);
}
