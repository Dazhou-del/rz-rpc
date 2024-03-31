package com.dazhou.rzrpc.core.fault.tolerant;

import com.dazhou.rzrpc.core.model.RpcResponse;

import java.util.Map;

/**
 * 故障转移策略
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-31 20:51
 */
public class FailOverTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //todo 获取其他服务节点并调用
        return null;
    }
}
