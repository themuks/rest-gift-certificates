package com.epam.esm.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
@EnableAutoConfiguration
public class SpringConfig {
    private static final String MESSAGES_BUNDLE = "messages";
    private static final String PACKAGE_NAME = "com.epam.esm";

    @Bean
    public ResourceBundleMessageSource resourceBundleMessageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename(MESSAGES_BUNDLE);
        return resourceBundleMessageSource;
    }

    @Bean
    public EntityManager entityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PACKAGE_NAME);
        return emf.createEntityManager();
    }
}
