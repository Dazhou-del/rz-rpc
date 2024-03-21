package com.dazhou.rzrpc.core.model;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务元信息(注册信息)
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-21 16:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceMetaInfo {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务地址
     */
//    private String serviceAddress;

    /**
     * 服务域名
     */
    private String serviceHost;

    /**
     * 服务端口号
     */
    private Integer servicePort;

    /**
     * 服务版本
     */
    private String serviceVersion;

    /**
     * 服务分组(带实现)
     */
    private String serviceGroup;

    /**
     * 获取服务注册名
     * @return
     */
    public String getServiceKey(){
        //分组待实现
//        return String.format("%s:%s:%s",serviceName,serviceVersion,serviceGroup);
        return String.format("%s:%s",serviceName,serviceVersion);
    }

    /**
     * 获取服务节点名
     * @return
     */
    public String getServiceNodeKey(){
        return String.format("%s:%s:%s",serviceName,serviceHost,servicePort);
    }

    /**
     * 获取完整服务地址
     * @return
     */
    public String getServiceAddress(){
        if(!StrUtil.contains(serviceHost,"http")){
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }
}
