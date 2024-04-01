package com.dazhou.rzrpc.core.proxy;

import cn.hutool.core.collection.CollUtil;
import com.dazhou.rzrpc.core.RpcApplication;
import com.dazhou.rzrpc.core.config.RpcConfig;
import com.dazhou.rzrpc.core.constant.RpcConstant;
import com.dazhou.rzrpc.core.fault.retry.RetryStrategy;
import com.dazhou.rzrpc.core.fault.retry.RetryStrategyFactory;
import com.dazhou.rzrpc.core.fault.tolerant.TolerantStrategy;
import com.dazhou.rzrpc.core.fault.tolerant.TolerantStrategyFactory;
import com.dazhou.rzrpc.core.loadbalancer.LoadBalancer;
import com.dazhou.rzrpc.core.loadbalancer.LoadBalancerFactory;
import com.dazhou.rzrpc.core.model.RpcRequest;
import com.dazhou.rzrpc.core.model.RpcResponse;
import com.dazhou.rzrpc.core.model.ServiceMetaInfo;
import com.dazhou.rzrpc.core.registry.Registry;
import com.dazhou.rzrpc.core.registry.RegistryFactory;
import com.dazhou.rzrpc.core.serializer.Serializer;
import com.dazhou.rzrpc.core.serializer.SerializerFactory;
import com.dazhou.rzrpc.core.serializer.SpiLoader;
import com.dazhou.rzrpc.core.service.tcp.VertxTcpClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * 动态代理
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 15:36
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        //从服务中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();

        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        //获取全部服务信息
        List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfos)) {
            throw new RuntimeException("暂无服务地址");
        }
        //负载均衡
        //获取具体实现类
        LoadBalancer balancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        // 将调用方法名（请求路径）作为负载均衡参数
        HashMap<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", rpcRequest.getMethodName());
        //调用负载均衡 获取具体的服务
        ServiceMetaInfo selectedServiceMetaInfo = balancer.selectService(requestParams, serviceMetaInfos);
        String serviceAddress = selectedServiceMetaInfo.getServiceAddress();
        log.info(serviceAddress);
        RpcResponse rpcResponse;
        try {
            //发起TCP请求
            //使用重试机制
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(selectedServiceMetaInfo, rpcRequest)
            );
        } catch (Exception e) {
            //容错策略
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            rpcResponse = tolerantStrategy.doTolerant(null, e);
        }
        return rpcResponse.getData();


    }

}



