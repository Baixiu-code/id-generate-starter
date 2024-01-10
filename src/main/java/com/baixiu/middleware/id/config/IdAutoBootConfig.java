package com.baixiu.middleware.id.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

/**
 * spring.factories 入口方法
 * 能力1：用以保证引入的业务方能够自动扫描本身的包体，扫描对应的bean到spring container
 * 能力2：用以扫描对应的 mybatis mapper 注入动态代理
 * @author baixiu
 * @date 创建时间 2023/12/22 3:59 PM
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@ComponentScan("com.baixiu.middleware.id")
@MapperScan("com.baixiu.middleware.id.dao")
public class IdAutoBootConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }
    
}
