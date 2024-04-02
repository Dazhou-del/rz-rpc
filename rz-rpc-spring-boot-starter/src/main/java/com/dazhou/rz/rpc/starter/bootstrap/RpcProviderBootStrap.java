package com.dazhou.rz.rpc.starter.bootstrap;

import com.dazhou.rz.rpc.starter.annotation.RpcService;
import com.dazhou.rzrpc.core.RpcApplication;
import com.dazhou.rzrpc.core.config.RegistryConfig;
import com.dazhou.rzrpc.core.config.RpcConfig;
import com.dazhou.rzrpc.core.model.ServiceMetaInfo;
import com.dazhou.rzrpc.core.registry.LocalRegistry;
import com.dazhou.rzrpc.core.registry.Registry;
import com.dazhou.rzrpc.core.registry.RegistryFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Rpc 服务提供者启动
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-01 21:24
 */
public class RpcProviderBootStrap implements BeanPostProcessor {
    /**
     * Bean 初始化后执行，注册服务
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService!=null){
            //需要注册服务
            //1.获取服务的基本信息
            Class<?> interfaceClass  = rpcService.interfaceClass();
            //默认值处理
            if (interfaceClass==void.class){
                interfaceClass=beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();
            //2.注册服务
            //本地注册
            LocalRegistry.register(serviceName,beanClass);
            //全局配置
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            //注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
