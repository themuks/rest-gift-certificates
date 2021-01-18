package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"com.epam.esm.model.dao.impl", "com.epam.esm.model.service.impl"})
@Import(DatabaseTestConfig.class)
@EnableTransactionManagement
public class SpringTestConfig {
    private final DatabaseTestConfig databaseTestConfig;

    public SpringTestConfig(DatabaseTestConfig databaseTestConfig) {
        this.databaseTestConfig = databaseTestConfig;
    }

    @Bean
    public PlatformTransactionManager transactionManagerBean() {
        return new DataSourceTransactionManager(databaseTestConfig.h2DataSource());
    }
}
