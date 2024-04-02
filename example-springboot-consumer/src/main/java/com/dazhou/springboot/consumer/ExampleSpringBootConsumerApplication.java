package com.dazhou.springboot.consumer;

import com.dazhou.rz.rpc.starter.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-02 11:57
 */
@SpringBootApplication
@EnableRpc(needServer = false)
public class ExampleSpringBootConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringBootConsumerApplication.class,args);
    }
}
