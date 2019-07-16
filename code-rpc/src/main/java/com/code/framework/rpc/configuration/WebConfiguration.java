package com.code.framework.rpc.configuration;

import com.code.framework.rpc.filter.RpcFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author SONGJIUHUA386
 * @since 2019/5/14
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

    /**
     * 配置rpc过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean rpcFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RpcFilter());
        registration.addUrlPatterns("/rpc");
        return registration;
    }


}
