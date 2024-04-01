package com.dazhou.rzrpc.core.bootstrap;

import com.dazhou.rzrpc.core.RpcApplication;
import com.dazhou.rzrpc.core.config.RpcConfig;
import com.dazhou.rzrpc.core.constant.RpcConstant;
import com.dazhou.rzrpc.core.model.ServiceMetaInfo;
import com.dazhou.rzrpc.core.model.ServiceRegisterInfo;
import com.dazhou.rzrpc.core.registry.LocalRegistry;
import com.dazhou.rzrpc.core.registry.Registry;
import com.dazhou.rzrpc.core.registry.RegistryFactory;
import com.dazhou.rzrpc.core.service.tcp.VertxTcpServer;

import java.util.List;

/**
 * 服务提供者-启动类
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-01 20:19
 */
public class ProviderBootstrap {


    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        //RPC框架初始化（配置和注册中心）
        RpcApplication.init();

        // 全局配置
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        //注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            // 本地注册
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImplClass());
            //注册到注册中心
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            //注册到注册中心
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }
        }

        //启动Tcp服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
