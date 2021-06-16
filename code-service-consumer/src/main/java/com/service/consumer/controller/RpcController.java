package com.service.consumer.controller;

import com.code.framework.rpc.client.RpcTestClient;
import com.code.framework.rpc.common.RpcClientContext;
import com.code.framework.rpc.common.RpcExporterContext;
import com.code.framework.rpc.common.RpcFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * @since 2019/5/14
 */
@RestController
public class RpcController {

    @Autowired
    private RpcTestClient rpcTestClient;
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;


    @RequestMapping(value = "/test")
    public Boolean test() throws NoSuchMethodException {
        RpcExporterContext exporterContext = new RpcExporterContext();
        try{
            Class clazz = Class.forName("com.code.framework.rpc.exporter.RpcTestServiceImpl");
            Method method = clazz.getDeclaredMethod("testRpcService", String.class);
            exporterContext.setBean(clazz);
            exporterContext.setMethod(method);
        }catch (Exception e){
            e.printStackTrace();
        }
        RpcFactory.registerExporter("RpcTestServiceImpl", "testRpcService", exporterContext);

        RpcClientContext clientContext = new RpcClientContext();
        //设置rpc服务端请求地址
        clientContext.setUrl("http://localhost:8080/rpc");
        //设置rpc服务端接口
        clientContext.setMethodName("RpcTestServiceImpl.testRpcService");
        RpcFactory.registerClient("RpcTestClient", "testClient", clientContext);
        String result = rpcTestClient.testClient("hhh");
        return Boolean.TRUE;
    }
    @RequestMapping(value = "/test/rpc")
    public String testRpc()  {

        return rpcTestClient.testClient("hhh");
    }


}
