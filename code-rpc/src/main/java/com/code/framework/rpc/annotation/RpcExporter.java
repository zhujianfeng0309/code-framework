package com.code.framework.rpc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * rpc 服务端接口注解
 * @author SONGJIUHUA386
 * @since 2019/5/13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcExporter {

    /**
     * RPC服务端处理器名称（接口名称）
     * @return
     */
    String value();

}
