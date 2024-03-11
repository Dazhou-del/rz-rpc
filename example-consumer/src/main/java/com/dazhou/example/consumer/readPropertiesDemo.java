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
//        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc","properties");
//        System.out.println(rpc);

//        //测试读取yml文件
        RpcConfig rpc2 = ConfigUtils.loadConfig(RpcConfig.class, "rpc","yml");
        System.out.println(rpc2);
//
//        //测试读取yaml文件
//        RpcConfig rpc3 = ConfigUtils.loadConfig(RpcConfig.class, "rpc","yaml");
//        System.out.println(rpc3);
    }
}
