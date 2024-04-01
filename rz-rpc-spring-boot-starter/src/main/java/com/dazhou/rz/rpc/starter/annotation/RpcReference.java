package com.dazhou.rz.rpc.starter.annotation;

import com.dazhou.rzrpc.core.constant.RpcConstant;
import com.dazhou.rzrpc.core.fault.retry.RetryStrategyKeys;
import com.dazhou.rzrpc.core.fault.tolerant.TolerantStrategyKeys;
import com.dazhou.rzrpc.core.loadbalancer.LoadBalancerKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务消费者（用于注入服务）
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-01 20:54
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {

    /**
     * 服务接口类
     *
     * @return
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 版本
     *
     * @return
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 负载均衡
     *
     * @return
     */
    String loadBalancer() default LoadBalancerKeys.RANDOM;

    /**
     * 重试策略
     *
     * @return
     */
    String retryStrategy() default RetryStrategyKeys.NO;

    /**
     * 容错策略
     */
    String tolerantStrategy() default TolerantStrategyKeys.FAIL_FAST;

    /**
     * 模拟调用
     */
    boolean mock() default false;
}
