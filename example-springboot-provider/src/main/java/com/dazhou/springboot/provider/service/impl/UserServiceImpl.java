package com.dazhou.springboot.provider.service.impl;

import com.dazhou.example.common.model.User;
import com.dazhou.example.common.service.UserService;
import com.dazhou.rz.rpc.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-02 11:54
 */
@Service
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("名字是："+user.getName());
        int b=33;
        System.out.println("数字是"+(b+user.getA()));
        return user;
    }
}
