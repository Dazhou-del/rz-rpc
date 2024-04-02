package com.dazhou.springboot.consumer.service.impl;

import com.dazhou.example.common.model.User;
import com.dazhou.example.common.service.UserService;
import com.dazhou.rz.rpc.starter.annotation.RpcReference;
import com.dazhou.rz.rpc.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-02 11:58
 */
@Service
public class ExampleConsumerService {

    @RpcReference
    private UserService userService;

    public void test(){
        User user = new User();
        user.setName("dazhou");
        user.setA(22);
        User resultUser = userService.getUser(user);
        System.out.println("你的名字是:"+resultUser.getName());
    }
}
