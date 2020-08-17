package com.senla.training_2019.smolka.controllers;

import com.senla.training_2019.smolka.web.utils.FileConfig;
import com.senla.training_2019.smolka.web.utils.TokenParser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan({
        "com.senla.training_2019.smolka.dao",
        "com.senla.training_2019.smolka.service",
        "com.senla.training_2019.smolka.mappers",
        "com.senla.training_2019.smolka.api.dao",
        "com.senla.training_2019.smolka.exception_handlers",
        "com.senla.training_2019.smolka.controllers",
        "com.senla.training_2019.smolka.web.utils"
}
)

@EnableJpaRepositories(basePackages = { "com.senla.training_2019.smolka.api.dao" })
@EntityScan(basePackages = { "com.senla.training_2019.smolka.model.entities" })
public class Runner {

    @Primary
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setLocation(new ClassPathResource("config.properties"));
        return propertySourcesPlaceholderConfigurer;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return mapper;
    }

    @Bean
    public FileConfig fileConfig() {
        return new FileConfig();
    }

//    @Bean
//    public TokenParser tokenParser() {
//        return new TokenParser(null, null);
//    }
//
//    @Bean
//    public PasswordEncoder getPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    public static void main(String[] args) {
        SpringApplication.run(Runner.class, args);
    }
}
