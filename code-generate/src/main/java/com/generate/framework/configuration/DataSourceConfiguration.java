package com.generate.framework.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean(name = "generateDataSource", destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties("spring.datasource.generate")
    @Primary
    public DataSource generateDataSource() {
        return new DruidDataSource();
    }

}