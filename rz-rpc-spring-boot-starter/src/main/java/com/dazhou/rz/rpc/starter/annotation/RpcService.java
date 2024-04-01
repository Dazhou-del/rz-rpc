package com.dazhou.rz.rpc.starter.annotation;

import com.dazhou.rzrpc.core.constant.RpcConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务提供者注解（用于注册服务）
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-01 20:49
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

    /**
     * 服务接口类
     * @return
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 版本
     * @return
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;
}
