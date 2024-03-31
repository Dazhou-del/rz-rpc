package com.dazhou.rzrpc.core.fault.tolerant;

import com.dazhou.rzrpc.core.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败容错
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-31 20:44
 */
public class FailFastTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错", e);
    }
}
