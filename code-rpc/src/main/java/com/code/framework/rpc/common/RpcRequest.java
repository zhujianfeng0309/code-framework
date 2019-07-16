package com.code.framework.rpc.common;

import java.util.List;

/**
 * @author SONGJIUHUA386
 * @since 2019/5/13
 */
public class RpcRequest {

    /**
     * 请求方法
     */
    private String method;

    /**
     * 参数
     */
    private List<Object> params;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }
}
