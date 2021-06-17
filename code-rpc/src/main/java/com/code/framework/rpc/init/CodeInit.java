package com.code.framework.rpc.init;

import com.code.framework.rpc.annotation.RpcClient;
import com.code.framework.rpc.annotation.RpcExporter;
import com.code.framework.rpc.common.RpcExporterContext;
import com.code.framework.rpc.common.RpcFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.Map;


public class CodeInit implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {

    // ---------------------- applicationContext ----------------------
    private  ApplicationContext applicationContext;
    @Override
    public void afterSingletonsInstantiated() {

    }

    /**
     * 将rpc客户端和服务端的注解配置信息 保存到RpcFactory中
     */
    private void resolveRpc() {
        // init handler from method
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);

            Map<Method, RpcExporter> annotatedRpcExporterMethods = null;
            // referred to ：org.springframework.context.event.EventListenerMethodProcessor.processBean
            Map<Method, RpcClient> annotatedRpcClientMethod = null;
            try {
                annotatedRpcExporterMethods = MethodIntrospector.selectMethods(bean.getClass(),
                        new MethodIntrospector.MetadataLookup<RpcExporter>() {
                            @Override
                            public RpcExporter inspect(Method method) {
                                return AnnotatedElementUtils.findMergedAnnotation(method, RpcExporter.class);
                            }
                        });
            } catch (Throwable ex) {

            }
            if (annotatedRpcExporterMethods == null || annotatedRpcExporterMethods.isEmpty()) {
                continue;
            }
            for (Map.Entry<Method, RpcExporter> methodXxlJobEntry : annotatedRpcExporterMethods.entrySet()) {
                Method executeMethod = methodXxlJobEntry.getKey();
                RpcExporter rpcExporter = methodXxlJobEntry.getValue();
                if (rpcExporter == null) {
                    continue;
                }
                RpcExporterContext exporterContext = new RpcExporterContext();
                exporterContext.setBean(bean);
                exporterContext.setMethod(executeMethod);
                RpcFactory.registerExporter(executeMethod.getName(), executeMethod.toString(), exporterContext);
            }
            //加载RpcClient
            try {
                annotatedRpcClientMethod = MethodIntrospector.selectMethods(bean.getClass(),
                        new MethodIntrospector.MetadataLookup<RpcClient>() {
                            @Override
                            public RpcClient inspect(Method method) {
                                return AnnotatedElementUtils.findMergedAnnotation(method, RpcClient.class);
                            }
                        });
            } catch (Throwable ex) {

            }
            if (annotatedRpcClientMethod == null || annotatedRpcClientMethod.isEmpty()) {
                continue;
            }
            for (Map.Entry<Method, RpcClient> methodXxlJobEntry : annotatedRpcClientMethod.entrySet()) {
                Method executeMethod = methodXxlJobEntry.getKey();
                RpcClient rpcClient = methodXxlJobEntry.getValue();
                if (rpcClient == null) {
                    continue;
                }
                RpcExporterContext exporterContext = new RpcExporterContext();
                exporterContext.setBean(bean);
                exporterContext.setMethod(executeMethod);
                RpcFactory.registerExporter(executeMethod.getName(), executeMethod.toString(), exporterContext);
            }
            /**
            for (Method method : methods) {
                //解析RPC服务端接口及配置
                RpcExporter rpcExporter = AnnotationUtils.findAnnotation(method, RpcExporter.class);
                if (rpcExporter != null) {
                    RpcExporterContext exporterContext = new RpcExporterContext();
                    exporterContext.setBean(bean);
                    exporterContext.setMethod(method);
                    RpcFactory.registerExporter(beanName, method.getName(), exporterContext);
                }
                //解析RPC客户端接口及配置
                RpcClient rpcClient = AnnotationUtils.findAnnotation(method, RpcClient.class);
                if (rpcClient != null) {
                    RpcClientContext clientContext = new RpcClientContext();
                    clientContext.setUrl(rpcClient.rpcExportUrl());
                    clientContext.setMethodName(rpcClient.value());
                    RpcFactory.registerClient(beanName, method.getName(), clientContext);
                }
            }*/
        }
    }
    @Override
    public void destroy()  {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
