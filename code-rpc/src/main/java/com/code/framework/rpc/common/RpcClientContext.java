package com.code.framework.rpc.common;

/**
 * @author SONGJIUHUA386
 * @since 2019/5/13
 */
public class RpcClientContext {

    /**
     * 服务端接口地址
     */
    private String url;

    /**
     * 服务端rpc接口名称（beanName.methodName）
     */
    private String methodName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
