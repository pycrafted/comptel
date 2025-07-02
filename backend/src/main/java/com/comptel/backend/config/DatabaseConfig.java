package com.comptel.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${SPRING_DATASOURCE_URL:}")
    private String dataSourceUrl;

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        
        // Convert Render's postgresql:// URL to jdbc:postgresql:// format
        if (dataSourceUrl != null && !dataSourceUrl.isEmpty() && dataSourceUrl.startsWith("postgresql://")) {
            String jdbcUrl = dataSourceUrl.replace("postgresql://", "jdbc:postgresql://");
            properties.setUrl(jdbcUrl);
        }
        
        return properties;
    }

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }
} 