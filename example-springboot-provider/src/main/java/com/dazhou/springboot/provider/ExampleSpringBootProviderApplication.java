package com.dazhou.springboot.provider;

import com.dazhou.rz.rpc.starter.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-02 11:47
 */
@SpringBootApplication
@EnableRpc
public class ExampleSpringBootProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringBootProviderApplication.class,args);
    }
}
