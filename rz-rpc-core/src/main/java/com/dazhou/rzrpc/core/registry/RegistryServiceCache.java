package com.dazhou.rzrpc.core.registry;

import com.dazhou.rzrpc.core.model.ServiceMetaInfo;

import java.util.List;

/**
 * 服务缓存
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-22 15:51
 */
public class RegistryServiceCache {

    /**
     * 缓存服务
     */
    List<ServiceMetaInfo> serviceCache;

    /**
     * 写缓存
     * @param newServiceCache
     */
    public void WriteCache(List<ServiceMetaInfo> newServiceCache){
        this.serviceCache=newServiceCache;
    }

    /**
     * 清缓存
     */
    public void clearCache(){
        this.serviceCache=null;
    }

    /**
     * 读缓存
     * @return
     */
    public List<ServiceMetaInfo> readCache(){
        return this.serviceCache;
    }
}
