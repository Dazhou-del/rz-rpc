package com.dazhou.rzrpc.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * 配置工具类
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-08 17:49
 */
public class ConfigUtils {

    /**
     * 加载配置对象
     * @param tClass
     * @param prefix
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass,String prefix){
        return loadConfig(tClass,prefix,"");
    }

    /**
     * 加载配置对象，支持区分环境
     * @param tClass
     * @param prefix
     * @param environment
     * @return
     * @param <T>
     */
    private static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {

        StringBuilder configFileBuilder  = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)){
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        //根据文件地址获取Props类
        Props props = new Props(configFileBuilder.toString());
        //读取配置转换成对应的bean对象
        return props.toBean(tClass, prefix);
    }
}








