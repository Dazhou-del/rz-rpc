package com.dazhou.example.common.service;

import com.dazhou.example.common.model.User;

/**
 * 用户服务
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 10:11
 */
public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);
}
