package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
@EnableTransactionManagement
@Import(DatabaseConfig.class)
public class SpringConfig {
    private static final String MESSAGES_BUNDLE = "messages";
    private final DatabaseConfig databaseConfig;

    public SpringConfig(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Bean
    public PlatformTransactionManager transactionManagerBean() {
        return new DataSourceTransactionManager(databaseConfig.mySqlDataSource());
    }

    @Bean
    public ResourceBundleMessageSource resourceBundleMessageSourceBean() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename(MESSAGES_BUNDLE);
        return resourceBundleMessageSource;
    }
}
