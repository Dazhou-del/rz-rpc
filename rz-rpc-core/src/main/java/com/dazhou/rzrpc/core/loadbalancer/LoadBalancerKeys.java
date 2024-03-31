package com.dazhou.rzrpc.core.loadbalancer;

/**
 * 负载均衡器常量
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-30 21:05
 */
public interface LoadBalancerKeys {
    /**
     * 按顺序轮询
     */
    String ROUND_ROBIN = "roundRobin";
    /**
     * 随机轮询
     */
    String RANDOM = "random";

    /**
     * 一致hasp算法轮询
     */
    String CONSISTENT_HASH = "consistentHash";
}
