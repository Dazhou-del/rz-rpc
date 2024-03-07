package com.dazhou.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.dazhou.example.common.model.User;
import com.dazhou.example.common.service.UserService;
import com.dazhou.rzrpc.model.RpcRequest;
import com.dazhou.rzrpc.model.RpcResponse;
import com.dazhou.rzrpc.serializer.JdkSerializer;

import java.io.IOException;

/**
 * 静态代理
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 15:15
 */
public class UserServiceProxy implements UserService {

    @Override
    public User getUser(User user) {
        //指定序列化器
        JdkSerializer jdkSerializer = new JdkSerializer();
        //构建请求对象
        RpcRequest rpcRequest=RpcRequest.builder()
                .ServiceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try {
            //将对象序列化准备发起请求
            byte[] bytes = jdkSerializer.serialize(rpcRequest);
            byte[] result;
            try {
                //发起请求获取结果
                HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                        .body(bytes)
                        .execute();
                result = httpResponse.bodyBytes();
                //反序列化
                RpcResponse rpcResponse = jdkSerializer.deserialize(result, RpcResponse.class);
                return (User) rpcResponse.getData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
