package com.dazhou.rzrpc.core.loadbalancer;

/**
 * 负载均衡器常量
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-30 21:05
 */
public interface LoadBalancerKeys {
    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    String RANDOM = "random";

    String CONSISTENT_HASH = "consistentHash";
}
