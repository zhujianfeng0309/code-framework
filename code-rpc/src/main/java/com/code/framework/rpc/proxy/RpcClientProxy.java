package com.code.framework.rpc.proxy;

import com.alibaba.fastjson.JSON;
import com.code.framework.rpc.common.RpcClientContext;
import com.code.framework.rpc.common.RpcFactory;
import com.code.framework.rpc.common.RpcRequest;
import com.code.framework.rpc.common.RpcResponse;
import com.code.framework.rpc.util.HttpUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author SONGJIUHUA386
 * @since 2019/5/13
 */
public class RpcClientProxy implements InvocationHandler {

    private Class<?> clazz;

    public RpcClientProxy(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * RPC客户端调用服务端入口
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = clazz.getSimpleName() + "." + method.getName();
        //获取rpc客户端接口方法注解中的配置
        RpcClientContext rpcClientContext = RpcFactory.getClientMap(methodName);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setMethod(rpcClientContext.getMethodName());
        rpcRequest.setParams(Arrays.asList(args));
        //通过httpClient请求rpc服务端接口
        String result = HttpUtil.doPost(rpcClientContext.getUrl(), JSON.toJSONString(rpcRequest));
        RpcResponse rpcResponse = JSON.parseObject(result, RpcResponse.class);
        return rpcResponse.getResult();
    }

    /**
     * 设置rpc客户端动态代理
     * @param clazz
     * @return
     */
    public static Object getProxy(Class<?> clazz){
        RpcClientProxy clientProxy = new RpcClientProxy(clazz);
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, clientProxy);
    }

}
