package com.code.framework.rpc.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 2019/5/13
 */
public class RpcFactory {

    /**
     * 服务端RPC接口
     */
    private static final Map<String, RpcExporterContext> EXPORTER_MAP = new HashMap<>();

    /**
     * 客户端RPC接口
     */
    private static final Map<String, RpcClientContext> CLIENT_MAP = new HashMap<>();

    /**
     * 保存RPC服务端接口基础信息
     * @param beanName
     * @param method
     * @param exporter
     */
    public static synchronized void registerExporter(String beanName, String method, RpcExporterContext exporter) {
        String key = getKey(beanName, method);
        EXPORTER_MAP.put(key, exporter);
    }

    /**
     * 保存RPC客户端接口基础信息
     * @param beanName
     * @param method
     * @param client
     */
    public static synchronized void registerClient(String beanName, String method, RpcClientContext client) {
        String key = getKey(beanName, method);
        CLIENT_MAP.put(key, client);
    }

    public static RpcExporterContext getExporterMap(String key){
        return EXPORTER_MAP.get(key);
    }

    public static RpcClientContext getClientMap(String key){
        return CLIENT_MAP.get(key);
    }

    private static String getKey(String beanName, String method) {
        return beanName + "." + method;
    }

}
