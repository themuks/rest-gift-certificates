package com.epam.esm.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ResourceBundle;

@Configuration
public class DatabaseTestConfig {
    private static final String DATABASE_BUNDLE_NAME = "database";
    private static final String DB_URL = "db.url";
    private static final String DB_DRIVER = "db.driver";

    @Bean
    public DataSource h2DataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        ResourceBundle resourceBundle = ResourceBundle.getBundle(DATABASE_BUNDLE_NAME);
        String url = resourceBundle.getString(DB_URL);
        String driver = resourceBundle.getString(DB_DRIVER);
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        return dataSource;
    }
}
