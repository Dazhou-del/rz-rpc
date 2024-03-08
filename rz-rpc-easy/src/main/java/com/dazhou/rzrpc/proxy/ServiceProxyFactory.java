package com.dazhou.rzrpc.proxy;

import java.lang.reflect.Proxy;

/**
 * 服务代理工厂(用于创建代理对象)
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 15:46
 */
public class ServiceProxyFactory {
    public static <T>T getProxy(Class<T> serviceClass){
        return (T) Proxy.newProxyInstance(
                //获取ServiceClass的类加载器。
                serviceClass.getClassLoader(),
                //指定要代理的接口，这里是ServiceClass。
                new Class[]{serviceClass},
                //指定代理实例的InvocationHandler，即ServiceProxy类的实例，该类负责处理代理对象的方法调用。
                new ServiceProxy()
        );

    }
}
