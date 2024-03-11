package com.dazhou.rzrpc.core.proxy;

import com.github.javafaker.Faker;
import com.github.javafaker.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 模拟代理类
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-11 15:35
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //根据方法的放回值类型，生成特定的默认对象
        Class<?> returnType = method.getReturnType();
        log.info("mock invoke {}", method.getName());
        return getDefaultObject(returnType);
    }

    /**
     * 生成指定类型的默认值对象
     * @param returnType
     * @return
     */
    private Object getDefaultObject(Class<?> returnType) {
        Faker faker = new Faker();
        PhoneNumber phoneNumber = faker.phoneNumber();
        //基本类型
        if (returnType.isPrimitive()){
            if (returnType == boolean.class) {
                return false;
            } else if (returnType == short.class) {
                return (short) 0;
            } else if (returnType == int.class) {
                return phoneNumber.hashCode();
            } else if (returnType == long.class) {
                return 0L;
            }
        }
        return phoneNumber.phoneNumber();
    }
}
