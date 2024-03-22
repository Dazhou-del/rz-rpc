package com.dazhou.rzrpc.core.registry;

import cn.hutool.json.JSONUtil;
import com.dazhou.rzrpc.core.Registry;
import com.dazhou.rzrpc.core.config.RegistryConfig;
import com.dazhou.rzrpc.core.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 注册中心具体实现
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-21 17:16
 */
public class EtcdRegistry implements Registry {
    private Client client;

    private KV kvClient;

    /**
     * 服务的根节点
     */
    private static final String ETCD_ROOT_PATH="/rpc/";

    @Override
    public void init(RegistryConfig registryConfig) {
        //获取Client 并且设置连接超时时间
         client = Client.builder().endpoints(registryConfig.getAddress())
                 .connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();
         kvClient=client.getKVClient();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception{
        //创建Lease和kv客户端
        Lease leaseClient = client.getLeaseClient();
        //创建一个60s的租约
        long leaseId = leaseClient.grant(600).get().getID();

        //设置要存储的键值对
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        //将键值对与租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key,value,putOption).get();
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registerKey,StandardCharsets.UTF_8));
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // 前缀搜索，结尾一定要加 '/'
        String searchPrefix = ETCD_ROOT_PATH + serviceKey;
        //+ "/";

        try {
            //构建前缀查询器
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            //前缀查询
            List<KeyValue> keyValueList = kvClient.get(ByteSequence.from(searchPrefix
                            , StandardCharsets.UTF_8), getOption)
                    .get()
                    .getKvs();
            //解析服务信息
            return keyValueList.stream()
                    .map(keyValue -> {
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败",e);
        }
    }


    @Override
    public void destory() {
        System.out.println("当前节点下线");
        if (kvClient!=null){
            kvClient.close();
        }
        if (client!=null){
            client.close();
        }
    }
}
