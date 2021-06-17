package com.service.consumer.client;


import com.code.framework.rpc.annotation.RpcClient;
import com.code.framework.rpc.annotation.RpcInterface;
import org.springframework.stereotype.Service;

/**
 * @since 2019/5/10
 */
@Service
@RpcInterface
public interface RpcTestClient {


    @RpcClient(value = "RpcTestServiceImpl.testRpcService", rpcExportUrl = "http://localhost:8888/rpc")
    String testClient(String params);

}
