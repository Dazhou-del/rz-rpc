package com.dazhou.example.consumer;

import com.dazhou.example.common.model.User;
import com.dazhou.example.common.service.UserService;
import com.dazhou.rzrpc.core.bootstrap.ConsumerBootstrap;
import com.dazhou.rzrpc.core.proxy.ServiceProxyFactory;

/**
 * 服务消费者示例项目
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-01 20:34
 */
public class ConsumerExample {
    public static void main(String[] args) {
        // 服务提供者初始化
        ConsumerBootstrap.init();

        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("yupi");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
