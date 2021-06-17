package com.code.framework.rpc.common;

import com.code.framework.rpc.annotation.RpcInterface;
import com.code.framework.rpc.proxy.RpcClientProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

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


}
