package com.dazhou.rzrpc.core.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.dazhou.rzrpc.core.Registry;
import com.dazhou.rzrpc.core.RpcApplication;
import com.dazhou.rzrpc.core.config.RpcConfig;
import com.dazhou.rzrpc.core.constant.RpcConstant;
import com.dazhou.rzrpc.core.model.RpcRequest;
import com.dazhou.rzrpc.core.model.RpcResponse;
import com.dazhou.rzrpc.core.model.ServiceMetaInfo;
import com.dazhou.rzrpc.core.registry.RegistryFactory;
import com.dazhou.rzrpc.core.serializer.JdkSerializer;
import com.dazhou.rzrpc.core.serializer.Serializer;
import com.dazhou.rzrpc.core.serializer.SerializerFactory;


import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 动态代理
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 15:36
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        Serializer serializer= SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            //这里地址被硬编码了（需要使用注册中心和服务发现机制解决）
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
            //暂时获取第一个
            ServiceMetaInfo serviceMetaInfo1 = serviceMetaInfos.get(0);
            // 发送请求
            try (HttpResponse httpResponse = HttpRequest.post(serviceMetaInfo1.getServiceAddress())
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                // 反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
