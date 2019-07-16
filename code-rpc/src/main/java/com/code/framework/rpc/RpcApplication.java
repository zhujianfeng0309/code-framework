package com.code.framework.rpc;

import com.code.framework.rpc.annotation.EnableRPC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author SONGJIUHUA386
 * @since 2019/5/14
 */
@SpringBootApplication
@EnableRPC(basePackages = {"com.code.framework.rpc"})
public class RpcApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcApplication.class, args);
    }
}
