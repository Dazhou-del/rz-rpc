package com.dazhou.rzrpc.core.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.dazhou.rzrpc.core.config.RpcConfig;


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
    public static <T> T loadConfig(Class<T> tClass,String prefix,String suffix){
        return loadConfig(tClass,prefix,"",suffix);
    }

    /**
     * 加载配置对象，支持区分环境
     * @param tClass
     * @param prefix
     * @param environment
     * @return
     * @param <T>
     */
    private static <T> T loadConfig(Class<T> tClass, String prefix, String environment,String suffix) {

        StringBuilder configFileBuilder  = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)){
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".");
        configFileBuilder.append(suffix);
        //根据文件地址获取Props类
        Props props = new Props(configFileBuilder.toString());
        //如果是yml文件
        if (StrUtil.equals(suffix,"yml")){
            return getRpcConfig(props);
        }
        if (StrUtil.equals(suffix,"yaml")){
            return getRpcConfig(props);
        }
        //读取配置转换成对应的bean对象
        return props.toBean(tClass, prefix);

    }

    private static <T> T getRpcConfig(Props props) {
        RpcConfig rpcConfig = new RpcConfig();
        if (ObjectUtil.isNotEmpty(props.get("name"))){
            rpcConfig.setName((String) props.get("name"));
        }
        if (ObjectUtil.isNotEmpty(props.get("version"))){
            rpcConfig.setVersion((String) props.get("version"));
        }
        if (ObjectUtil.isNotEmpty(props.get("serverHost"))){
            rpcConfig.setServerHost((String) props.get("serverHost"));
        }
        if (ObjectUtil.isNotEmpty(props.get("serverPort"))){
            String o = (String) props.get("serverPort");
            rpcConfig.setServerPort(Integer.valueOf(o));
        }
        return (T) rpcConfig;
    }

}








