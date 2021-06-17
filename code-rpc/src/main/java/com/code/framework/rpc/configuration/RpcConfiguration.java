package com.code.framework.rpc.configuration;

import com.code.framework.rpc.annotation.EnableRPC;
import com.code.framework.rpc.common.RpcRegister;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class RpcConfiguration {

    private static Set<String> basePackages = new LinkedHashSet<>();

    /**
     * basePackages路径下的rpc客户端接口，设置为动态代理
     * @return
     */
    @Bean
    public RpcRegister rpcRegister(){
        return new RpcRegister(basePackages);
    }

    public static class RpcPackageRegister implements ImportBeanDefinitionRegistrar{
        /**
         * 解析EnableRPC basePackages参数
         * @param metadata
         * @param registry
         */
        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            Map<String, Object> enableMap = metadata.getAnnotationAttributes(EnableRPC.class.getName());
            AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(enableMap);
            String[] packages = annotationAttributes.getStringArray("basePackages");
            for (String basePackage : packages){
                String[] tokenize = StringUtils.tokenizeToStringArray(basePackage,
                        ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
                basePackages.addAll(Arrays.asList(tokenize));
            }
            if (basePackages.isEmpty()) {
                basePackages.add(ClassUtils.getPackageName(metadata.getClassName()));
            }
        }
    }

}
