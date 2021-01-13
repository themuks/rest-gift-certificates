package com.epam.esm.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ResourceBundle;

@Configuration
public class DatabaseConfig {
    private static final String DATABASE_BUNDLE_NAME = "database";
    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_DRIVER = "db.driver";
    private static final String DB_POOL_SIZE = "db.poolSize";

    @Bean
    public DataSource mySqlDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        ResourceBundle resourceBundle = ResourceBundle.getBundle(DATABASE_BUNDLE_NAME);
        String url = resourceBundle.getString(DB_URL);
        String user = resourceBundle.getString(DB_USER);
        String password = resourceBundle.getString(DB_PASSWORD);
        String driver = resourceBundle.getString(DB_DRIVER);
        String poolSizeStr = resourceBundle.getString(DB_POOL_SIZE);
        int poolSize = Integer.parseInt(poolSizeStr);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setInitialSize(poolSize);
        return dataSource;
    }
}
