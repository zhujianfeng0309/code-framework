package com.service.provide.service;

import com.code.framework.rpc.annotation.RpcExporter;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @since 2019/5/10
 */
@Service
public class RpcTestServiceImpl implements RpcTestService{

    private final static Logger LOGGER = getLogger(RpcTestServiceImpl.class);

    @Override
    @RpcExporter(value = "testRpcService")
    public Boolean testRpcService(String params){
        LOGGER.info("======testRpcService====success====" + params);
        return true;
    }

}
