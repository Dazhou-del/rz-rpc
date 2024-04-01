package com.dazhou.rz.rpc.starter.bootstrap;

import com.dazhou.rz.rpc.starter.annotation.EnableRpc;
import com.dazhou.rzrpc.core.RpcApplication;
import com.dazhou.rzrpc.core.config.RpcConfig;
import com.dazhou.rzrpc.core.service.tcp.VertxTcpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Rpc 框架启动
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-01 20:59
 */
@Slf4j
public class RpcInitBootStrap implements ImportBeanDefinitionRegistrar {

    /**
     * Spring在初始化时执行，初始化RPC框架
     * @param importingClassMetadata
     * @param registry
     * @param importBeanNameGenerator
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        //获取EnableRpc注解的属性值
        Boolean needServer = (Boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName())
                .get("needServer");
        //rpc框架初始化
        RpcApplication.init();
        //全局配置
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        if (needServer){
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        }else {
            log.info("不启动 server");
        }
    }
}
