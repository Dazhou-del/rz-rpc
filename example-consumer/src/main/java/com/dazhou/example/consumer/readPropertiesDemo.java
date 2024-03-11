package com.dazhou.example.consumer;

import com.dazhou.rzrpc.core.config.RpcConfig;
import com.dazhou.rzrpc.core.utils.ConfigUtils;

/**
 * 读取配置测试
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-11 11:35
 */
public class readPropertiesDemo {

    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
