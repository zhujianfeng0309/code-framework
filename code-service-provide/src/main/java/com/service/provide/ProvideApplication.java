package com.service.provide;

import com.code.framework.rpc.annotation.EnableRPC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @since 2019/5/14
 */
@SpringBootApplication
@EnableRPC(basePackages = {"com.service.provide"})
public class ProvideApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProvideApplication.class, args);
    }
}
