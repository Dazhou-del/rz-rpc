package com.dazhou.rzrpc.core.serializer;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI 加载器（支持键值对映射）
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-19 16:44
 */
@Slf4j
public class SpiLoader {

    /**
     * 存储已加载的类：接口名 =>（key => 实现类）  懒加载
     */
    private static Map<String, Map<String, Class<?>>> loaderMap=new ConcurrentHashMap<>();


    /**
     * 对象实例缓存（避免重复 new），类路径 => 对象实例，单例模式
     */
    private static Map<String,Object> instanceCache=new ConcurrentHashMap<>();


    /**
     * 系统 SPI 目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义 SPI 目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    /**
     * 动态加载的类列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);


    /**
     * 加载所有类型
     */
    public static void loadAll() {
        log.info("加载所有 SPI");
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }

    /**
     * 获取某个接口的实例
     *
     * @param tClass
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<?> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader 未加载 %s 类型", tClassName));
        }
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader 的 %s 不存在 key=%s 的类型", tClassName, key));
        }
        // 获取到要加载的实现类型
        Class<?> implClass = keyClassMap.get(key);
        // 从实例缓存中加载指定类型的实例
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)) {
//            synchronized (SpiLoader.class) {
                try {
                    //如果缓存中没有这个key对应的实例 则将实例放入缓存中 implClass.newInstance()获取class的实例
                    instanceCache.put(implClassName, implClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    String errorMsg = String.format("%s 类实例化失败", implClassName);
                    throw new RuntimeException(errorMsg, e);
                }
//            }
        }
        T t = (T) instanceCache.get(implClassName);
        return t;
    }

    /**
     * 加载某个类型
     *
     * @param loadClass
     * @throws IOException
     */
    public static Map<String, Class<?>> load(Class<?> loadClass) {
        log.info("加载类型为 {} 的 SPI", loadClass.getName());
        // 扫描路径，用户自定义的 SPI 优先级高于系统 SPI
        Map<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            String loadClassName = loadClass.getName();
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());
//            if (resources.size()==0){
//                continue;
//            }
            // 读取每个资源文件
            for (URL resource : resources) {
                try {
                    //获取流
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    //获取Buffer
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    //从Buffer中读取数据
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] strArray = line.split("=");
                        if (strArray.length > 1) {
                            String key = strArray[0];  //例如可能=jdk
                            String className = strArray[1]; //可能等于=com.dazhou.rzrpc.core.serializer.JdkSerializer
                            //（Class.forName）返回与给定的字符串名称相关联类或接口的Class对象。
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    log.error("spi resource load error", e);
                }
            }
        }
        //数据可能是com.dazhou.rzrpc.core.serializer,(jdk,JdkSerializer.class);
        loaderMap.put(loadClass.getName(), keyClassMap);
        return keyClassMap;
    }



}
