package com.dazhou.rz.rpc.starter.annotation;

import com.dazhou.rz.rpc.starter.bootstrap.RpcConsumerBootstrap;
import com.dazhou.rz.rpc.starter.bootstrap.RpcInitBootStrap;
import com.dazhou.rz.rpc.starter.bootstrap.RpcProviderBootStrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用rpc注解
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-01 20:46
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootStrap.class, RpcProviderBootStrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {

    /**
     * 是否需要启动server
     * @return
     */
    boolean needServer() default true;
}
