package com.dazhou.rzrpc.core.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.dazhou.rzrpc.core.protocol.*;
import com.dazhou.rzrpc.core.registry.Registry;
import com.dazhou.rzrpc.core.RpcApplication;
import com.dazhou.rzrpc.core.config.RpcConfig;
import com.dazhou.rzrpc.core.constant.RpcConstant;
import com.dazhou.rzrpc.core.model.RpcRequest;
import com.dazhou.rzrpc.core.model.RpcResponse;
import com.dazhou.rzrpc.core.model.ServiceMetaInfo;
import com.dazhou.rzrpc.core.registry.RegistryFactory;
import com.dazhou.rzrpc.core.serializer.Serializer;
import com.dazhou.rzrpc.core.serializer.SerializerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

/**
 * 动态代理
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 15:36
 */
@Slf4j
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
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfos.get(0);
            String serviceAddress = selectedServiceMetaInfo.getServiceAddress();
            log.info(serviceAddress);


/*            // Http请求发送请求
            try (HttpResponse httpResponse = HttpRequest.post(serviceAddress)
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                // 反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
            //发起TCP请求
            Vertx vertx = Vertx.vertx();
            NetClient netClient = vertx.createNetClient();
            CompletableFuture<RpcResponse> responseFuture  = new CompletableFuture<>();
            netClient.connect(selectedServiceMetaInfo.getServicePort(),selectedServiceMetaInfo.getServiceHost(),
                    result->{
                        if (result.succeeded()){
                            System.out.println("Connected to TCP server");
                            NetSocket socket = result.result();
                            //发送消息
                            //构建请求
                            ProtocolMessage<RpcRequest> protocolMessage  = new ProtocolMessage<>();
                            ProtocolMessage.Header header = new ProtocolMessage.Header();
                            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                            header.setRequestId(IdUtil.getSnowflakeNextId());
                            protocolMessage.setHeader(header);
                            protocolMessage.setBody(rpcRequest);
                            try {
                                //编码请求
                                Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
                                socket.write(encode);
                            } catch (IOException e) {
                                throw new RuntimeException("协议消息编码错误");
                            }
                            //接收响应
                            socket.handler(buffer -> {
                                //解码
                                try {
                                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage =
                                            (ProtocolMessage<RpcResponse>)ProtocolMessageDecoder.decode(buffer);
                                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
                                } catch (IOException e) {
                                    throw new RuntimeException("协议消息解码错误");
                                }
                            });
                        }else {
                            System.err.println("Failed to connect to TCP server");
                        }
            });
            RpcResponse rpcResponse = responseFuture.get();
            //关闭连接
            netClient.close();
            return rpcResponse.getData();
        }catch (IOException e) {
            e.printStackTrace();
        }

            return null;
    }
}
