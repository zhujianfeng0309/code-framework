package com.code.framework.rpc.common;

import com.code.framework.rpc.annotation.RpcClient;
import com.code.framework.rpc.annotation.RpcExporter;
import com.code.framework.rpc.annotation.RpcInterface;
import com.code.framework.rpc.proxy.RpcClientProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @since 2019/5/13
 */
public class RpcRegister implements  BeanFactoryPostProcessor {

    /**
     * rpc扫描包
     */
    private Set<String> baseScanPackages;

    public RpcRegister(Set<String> baseScanPackages) {
        this.baseScanPackages = baseScanPackages;
    }

    /**
     * spring容器在初始化bean之前，允许自定义某些bean
     * RPC客户端所有bean设置动态代理，调用时走动态代理的invoke()
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
        for (String basePackage : baseScanPackages){
            try {
                Set<Class<?>> classSet = scanBackPackage(basePackage);
                for (Class<?> clazz : classSet){
                    if (clazz.isInterface() && clazz.isAnnotationPresent(RpcInterface.class)){
                        //生成动态代理
                        Object proxy = RpcClientProxy.getProxy(clazz);
                        //将bean注入到spring容器中
                        factory.registerSingleton(clazz.getName(), proxy);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 扫描包下面的所有类
     * @param basePackage
     * @return
     */
    private Set<Class<?>> scanBackPackage(String basePackage){
        Set<Class<?>> classSet = new HashSet<>();
        try{
            String pattern = "classpath*:" + ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class";
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources){
                if (resource.isReadable()){
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    String className = reader.getClassMetadata().getClassName();
                    classSet.add(Class.forName(className, false, Thread.currentThread().getContextClassLoader()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return classSet;
    }

    /**
     * 将rpc客户端和服务端的注解配置信息 保存到RpcFactory中
     * @param bean
     * @param beanName
     */
    private void resolveRpc(Object bean, String beanName){
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        if (methods == null){
            return;
        }
        for (Method method : methods){
            //解析RPC服务端接口及配置
            RpcExporter rpcExporter = AnnotationUtils.findAnnotation(method, RpcExporter.class);
            if (rpcExporter != null){
                RpcExporterContext exporterContext = new RpcExporterContext();
                exporterContext.setBean(bean);
                exporterContext.setMethod(method);
                RpcFactory.registerExporter(beanName, method.getName(), exporterContext);
            }
            //解析RPC客户端接口及配置
            RpcClient rpcClient = AnnotationUtils.findAnnotation(method, RpcClient.class);
            if (rpcClient != null){
                RpcClientContext clientContext = new RpcClientContext();
                clientContext.setUrl(rpcClient.rpcExportUrl());
                clientContext.setMethodName(rpcClient.value());
                RpcFactory.registerClient(beanName, method.getName(), clientContext);
            }
        }
    }
}
