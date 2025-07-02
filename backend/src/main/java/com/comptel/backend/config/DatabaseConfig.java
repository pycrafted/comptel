package com.comptel.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class DatabaseConfig {

    @Value("${SPRING_DATASOURCE_URL:}")
    private String dataSourceUrl;

    @Value("${SPRING_DATASOURCE_USERNAME:}")
    private String dataSourceUsername;

    @Value("${SPRING_DATASOURCE_PASSWORD:}")
    private String dataSourcePassword;

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        
        // Handle Render's database URL format
        if (dataSourceUrl != null && !dataSourceUrl.isEmpty()) {
            String jdbcUrl = buildJdbcUrl();
            properties.setUrl(jdbcUrl);
            System.out.println("🔧 Database URL configured: " + jdbcUrl.replaceAll(":[^:@]*@", ":***@"));
        }
        
        return properties;
    }

    private String buildJdbcUrl() {
        if (dataSourceUrl.startsWith("postgresql://")) {
            // Parse Render's postgresql:// URL
            String urlWithoutPrefix = dataSourceUrl.substring("postgresql://".length());
            int atIndex = urlWithoutPrefix.indexOf('@');
            
            if (atIndex > 0) {
                String credentials = urlWithoutPrefix.substring(0, atIndex);
                String hostAndDb = urlWithoutPrefix.substring(atIndex + 1);
                
                int colonIndex = credentials.indexOf(':');
                if (colonIndex > 0) {
                    String username = credentials.substring(0, colonIndex);
                    String password = credentials.substring(colonIndex + 1);
                    
                    // URL encode the password to handle special characters
                    String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8);
                    
                    return "jdbc:postgresql://" + hostAndDb + "?user=" + username + "&password=" + encodedPassword;
                }
            }
            
            // Fallback: simple replacement
            return dataSourceUrl.replace("postgresql://", "jdbc:postgresql://");
        }
        
        return dataSourceUrl;
    }

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }
} 