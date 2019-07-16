package com.code.framework.rpc.annotation;


import com.code.framework.rpc.configuration.RpcConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author SONGJIUHUA386
 * @since 2019/5/13
 */
@Documented
@Configuration
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcConfiguration.RpcPackageRegister.class, RpcConfiguration.class})
public @interface EnableRPC {

    /**
     * RPC 扫描包路径
     * @return
     */
    String[] basePackages() default {};

}



