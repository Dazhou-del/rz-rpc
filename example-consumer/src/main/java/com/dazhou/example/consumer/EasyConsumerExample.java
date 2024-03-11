package com.dazhou.example.consumer;

import com.dazhou.example.common.model.User;
import com.dazhou.example.common.service.UserService;
import com.dazhou.rzrpc.core.proxy.ServiceProxyFactory;


/**
 * 简易消费者示例
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 10:22
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        //需要获取UserService的实现类对象
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("daZhou");
        //调用
        User newUser = userService.getUser(user);
        if(newUser!=null){
            System.out.println(newUser.getName());
        }else {
            System.out.println("user=null");
        }
        //测试模拟方法
        long number = userService.getNumber();
        System.out.println(number);
    }
}
