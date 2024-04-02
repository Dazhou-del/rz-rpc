package com.dazhou.sprinboot.consumer;

import com.dazhou.springboot.consumer.ExampleSpringBootConsumerApplication;
import com.dazhou.springboot.consumer.service.impl.ExampleConsumerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-02 14:19
 */
@SpringBootTest(classes = {ExampleSpringBootConsumerApplication.class})
class ExampleConsumerTest {

    @Resource
    private ExampleConsumerService exampleConsumerService;

    @Test
    void test1(){
        exampleConsumerService.test();
    }
}
