package com.dazhou.rzrpc.core.registry;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import com.dazhou.rzrpc.core.config.RegistryConfig;
import com.dazhou.rzrpc.core.model.ServiceMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * zooKeeper注册中心
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-22 17:19
 */
@Slf4j
public class ZooKeeperRegistry implements Registry{

    private CuratorFramework client;

    private ServiceDiscovery<ServiceMetaInfo> serviceDiscovery;

    /**
     * 本机注册的节点 key 集合（用于维护续期）
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 注册中心服务缓存
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    /**
     * 正在监听的 key 集合
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    /**
     * 根节点
     */
    private static final String ZK_ROOT_PATH = "/rpc/zk";


    @Override
    public void init(RegistryConfig registryConfig) {
        //构建client实例
        client = CuratorFrameworkFactory.builder()
                .connectString(registryConfig.getAddress())
                .retryPolicy(new
                        ExponentialBackoffRetry(Math.toIntExact(registryConfig.getTimeout()), 3))
                .build();

        //构建serviceDiscovery 实例
         serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMetaInfo.class)
                .client(client)
                .basePath(ZK_ROOT_PATH)
                .serializer(new JsonInstanceSerializer<>(ServiceMetaInfo.class))
                .build();
        //启动client和serviceDiscovery
        try {
            client.start();
            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //注册到zk中
        serviceDiscovery.registerService(buildServiceInstance(serviceMetaInfo));

        // 添加节点信息到本地缓存
        String registerKey = ZK_ROOT_PATH + "/" + serviceMetaInfo.getServiceNodeKey();
        localRegisterNodeKeySet.add(registerKey);

    }
    private ServiceInstance<ServiceMetaInfo> buildServiceInstance(ServiceMetaInfo serviceMetaInfo) {
        String serviceAddress = serviceMetaInfo.getServiceHost() + ":" + serviceMetaInfo.getServicePort();
        try {
            return ServiceInstance
                    .<ServiceMetaInfo>builder()
                    .id(serviceAddress)
                    .name(serviceMetaInfo.getServiceKey())
                    .address(serviceAddress)
                    .payload(serviceMetaInfo)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        try {
            serviceDiscovery.unregisterService(buildServiceInstance(serviceMetaInfo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 从本地缓存移除
        String registerKey = ZK_ROOT_PATH + "/" + serviceMetaInfo.getServiceNodeKey();
        localRegisterNodeKeySet.remove(registerKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey)  {
        //从缓存中获取
        List<ServiceMetaInfo> serviceMetaInfoList = registryServiceCache.readCache();
        if (!CollectionUtil.isEmpty(serviceMetaInfoList)){
            return serviceMetaInfoList;
        }
        //从注册中心获取
        try {
            // 查询服务信息
            Collection<ServiceInstance<ServiceMetaInfo>> serviceInstances = serviceDiscovery.queryForInstances(serviceKey);

            //解析服务信息
            List<ServiceMetaInfo> serviceMetaInfos = serviceInstances.stream()
                    .map(ServiceInstance::getPayload)
                    .collect(Collectors.toList());
            //放入缓存
            registryServiceCache.WriteCache(serviceMetaInfos);
            return serviceMetaInfos;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    @Override
    public void destory() {
        log.info("当前节点下线");
        // 下线节点（这一步可以不做，因为都是临时节点，服务下线，自然就被删掉了）
        for (String key : localRegisterNodeKeySet) {
            try {
                client.delete().guaranteed().forPath(key);
            } catch (Exception e) {
                throw new RuntimeException(key + "节点下线失败");
            }
        }

        // 释放资源
        if (client != null) {
            client.close();
        }
    }

    @Override
    public void heartBeat() {
        // 不需要心跳机制，建立了临时节点，如果服务器故障，则临时节点直接丢失
    }

    @Override
    public void watch(String serviceNodeKey) {
        String watchKey=ZK_ROOT_PATH+serviceNodeKey;
        boolean newWatch  = watchingKeySet.add(watchKey);
        if (newWatch){
            CuratorCache curatorCache = CuratorCache.build(client, watchKey);
            //启动监听
            curatorCache.start();
            curatorCache.listenable().addListener(
                    CuratorCacheListener.builder()
                            //删除时清空缓存
                            .forDeletes(childData -> registryServiceCache.clearCache())
                            //更改时清空缓存
                            .forChanges(((oldNode,node)->
                                    registryServiceCache.clearCache()
                                    ))
                            .build()
            );
        }
    }
}
