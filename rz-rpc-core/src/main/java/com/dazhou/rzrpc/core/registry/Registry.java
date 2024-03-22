package com.dazhou.rzrpc.core.registry;

import com.dazhou.rzrpc.core.config.RegistryConfig;
import com.dazhou.rzrpc.core.model.ServiceMetaInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 注册中心
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-21 17:07
 */
public interface Registry {

    /**
     * 初始化
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 服务注册
     * @param serviceMetaInfo
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现(获取某服务的所有节点，消费端)
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务销毁
     */
    void destory();

    /**
     * 心跳检测（服务端）
     */
    void heartBeat();

    /**
     * 监听消费者
     * @param serviceNodeKey
     */
    void watch(String serviceNodeKey);
}
