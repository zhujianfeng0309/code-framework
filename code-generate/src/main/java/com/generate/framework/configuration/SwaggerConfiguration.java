package com.generate.framework.configuration;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author SONGJIUHUA386
 * @since 2019/7/11
 */
@Configuration
public class SwaggerConfiguration implements ApplicationListener<WebServerInitializedEvent> {


    @Bean
    public Docket createWebApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                       .apis(RequestHandlerSelectors.basePackage("com.generate.framework"))
                       .paths(PathSelectors.any())
                       .build().groupName("web接口")
                       .apiInfo(apiInfo()).useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("自动生成代码", "", "songjiuhua");
        return new ApiInfoBuilder().title("接口文档").contact(contact).description("接口文档").build();

    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        int serverPort = webServerInitializedEvent.getWebServer().getPort();
        System.err.println("swagger访问地址:http://" + getAddress() + ":" + serverPort + "/swagger-ui.html");
    }

    private String getAddress() {
        String hostAddress;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "127.0.0.1";
        }
        return hostAddress;
    }
}
