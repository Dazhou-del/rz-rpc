package com.dazhou.example.provider;

import com.dazhou.example.common.service.UserService;
import com.dazhou.rzrpc.core.bootstrap.ProviderBootstrap;
import com.dazhou.rzrpc.core.model.ServiceRegisterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务提供者示例
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-01 20:29
 */
public class ProviderExample {
    public static void main(String[] args) {
        // 要注册的服务
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
