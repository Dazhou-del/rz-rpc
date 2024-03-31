package com.dazhou.retry;

import com.dazhou.rzrpc.core.fault.retry.FixedIntervalRetryStrategy;
import com.dazhou.rzrpc.core.fault.retry.NoRetryStrategy;
import com.dazhou.rzrpc.core.fault.retry.RetryStrategy;
import com.dazhou.rzrpc.core.model.RpcResponse;
import org.junit.jupiter.api.Test;

/**
 * 重试策略测试
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-31 16:56
 */
public class RetryStrategyTest {
    RetryStrategy retryStrategy=new FixedIntervalRetryStrategy();

    @Test
    public void doRetry() {
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }
}
