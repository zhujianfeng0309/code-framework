package com.code.framework.rpc.common;

import java.lang.reflect.Method;

/**
 * @since 2019/5/13
 */
public class RpcExporterContext {

    /**
     * rpc接口服务端bean
     */
    private Object bean;
    /**
     * rpc服务端接口方法
     */
    private Method method;

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
