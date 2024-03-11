package com.dazhou.rzrpc.core;

import com.dazhou.rzrpc.core.config.RpcConfig;
import com.dazhou.rzrpc.core.constant.RpcConstant;
import com.dazhou.rzrpc.core.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * RPC应用框架
 * 相当于holder，存放了项目全局用到的变量，双检锁单例模式实现
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-11 11:10
 */
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化，支持传入自定义配置
     * @param newrRpcConfig
     */
    public static void init(RpcConfig newrRpcConfig){
        rpcConfig=newrRpcConfig;
        log.info("rpc init, config = {}", newrRpcConfig.toString());
    }


    public static  void init(){
       RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 配置加载失败，使用默认值
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);

    }

    public static RpcConfig getRpcConfig(){
        if(rpcConfig==null){
            synchronized(RpcApplication.class){
                if (rpcConfig==null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
