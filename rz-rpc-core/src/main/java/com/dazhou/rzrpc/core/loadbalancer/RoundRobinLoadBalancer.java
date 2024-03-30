package com.dazhou.rzrpc.core.loadbalancer;

import com.dazhou.rzrpc.core.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-30 20:35
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo selectService(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        //无服务不选择
        if (size == 0) {
            return null;
        }
        //只有一个服务无需轮询
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        //getAndIncrement 先返回再加一
        int index = currentIndex.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
