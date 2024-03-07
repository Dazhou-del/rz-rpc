package com.dazhou.rzrpc.service;

import cn.hutool.core.util.ObjectUtil;
import com.dazhou.rzrpc.model.RpcRequest;
import com.dazhou.rzrpc.model.RpcResponse;
import com.dazhou.rzrpc.registry.LocalRegistry;
import com.dazhou.rzrpc.serializer.JdkSerializer;
import com.dazhou.rzrpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * HTTP请求处理器
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 14:37
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        //指定序列化器
        final Serializer serializer=new JdkSerializer();

        //记录日志
        System.out.println("Received request: " + request.method() + " " + request.uri());


        //异步处理HTTP请求
        request.bodyHandler(body->{
            //获取请求的字节数组
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest=null;
            //将字节数组转换为java对象
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //构建响应结果对象
            RpcResponse rpcResponse=new RpcResponse();
            //如果请求为null直接返回
            if (ObjectUtil.isEmpty(request)){
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }
            try {
                // 获取要调用的服务实现类，通过反射调用
                Class<?> aClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = aClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(aClass.newInstance(), rpcRequest.getArgs());
                //封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setMessage("ok");
                rpcResponse.setDataType(method.getReturnType());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            doResponse(request,rpcResponse, serializer);


        });
    }

    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response().putHeader("content-type", "application/json");
        try {
            //序列化
            byte[] serialize = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialize));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
