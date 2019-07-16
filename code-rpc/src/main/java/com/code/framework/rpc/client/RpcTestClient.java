package com.code.framework.rpc.client;


import com.code.framework.rpc.annotation.RpcClient;
import com.code.framework.rpc.annotation.RpcInterface;
import org.springframework.stereotype.Service;

/**
 * @author SONGJIUHUA386
 * @since 2019/5/10
 */
@Service
@RpcInterface
public interface RpcTestClient {


    @RpcClient(value = "RpcTestServiceImpl.testRpcService", rpcExportUrl = "http://localhost:8080/rpc")
    Boolean testClient(String params);

}
