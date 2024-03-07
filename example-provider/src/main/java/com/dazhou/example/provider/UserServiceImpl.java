package com.dazhou.example.provider;

import com.dazhou.example.common.model.User;
import com.dazhou.example.common.service.UserService;

/**
 * 用户服务实现类
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 10:18
 */
public class UserServiceImpl implements UserService {
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
