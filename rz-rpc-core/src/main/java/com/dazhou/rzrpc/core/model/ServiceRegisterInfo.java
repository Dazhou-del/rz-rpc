package com.dazhou.rzrpc.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务注册信息类
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-01 20:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRegisterInfo<T> {
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 实现类
     */
    private Class<? extends T> implClass;
}
