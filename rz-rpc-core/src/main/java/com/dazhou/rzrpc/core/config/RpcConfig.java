package com.dazhou.rzrpc.core.config;

import com.dazhou.rzrpc.core.serializer.SerializerKeys;
import lombok.Data;

/**
 * RPC框架配置
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-08 17:34
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name="da-zhou";

    /**
     * 版本号
     */
    private String version="1.0";

    /**
     * 服务器主机名
     */
    private String serverHost="localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort=8080;

    /**
     * 是否开启Mock
     */
    private Boolean mock=false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册配置
     */
    private RegistryConfig registryConfig;
}
