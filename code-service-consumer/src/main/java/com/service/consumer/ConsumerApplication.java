package com.service.consumer;

import com.code.framework.rpc.annotation.EnableRPC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @since 2019/5/14
 */
@SpringBootApplication
@EnableRPC(basePackages = {"com.service.consumer.client"})
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
