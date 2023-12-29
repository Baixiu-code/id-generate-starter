package com.baixiu.middleware.id.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

/**
 * @author baixiu
 * @date 创建时间 2023/12/22 3:53 PM
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConfig {
    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

}
