package com.epam.esm;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;

@SpringBootApplication
public class GiftCertificatesApplication {
    private static final String MESSAGES_BUNDLE = "messages";

    public static void main(String[] args) {
        SpringApplication.run(GiftCertificatesApplication.class, args);
    }

    @Bean
    public ResourceBundleMessageSource resourceBundleMessageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename(MESSAGES_BUNDLE);
        return resourceBundleMessageSource;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
