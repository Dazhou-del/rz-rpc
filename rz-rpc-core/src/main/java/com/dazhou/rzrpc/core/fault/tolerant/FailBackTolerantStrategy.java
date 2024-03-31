package com.dazhou.rzrpc.core.fault.tolerant;

import com.dazhou.rzrpc.core.model.RpcResponse;

import java.util.Map;

/**
 * 降级到其他服务 - 容错策略
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-31 20:49
 */
public class FailBackTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //todo 获取降级的服务并调用
        return null;
    }
}
