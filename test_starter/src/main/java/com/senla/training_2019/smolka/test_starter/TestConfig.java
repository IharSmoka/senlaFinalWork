package com.senla.training_2019.smolka.test_starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TestStarterProperties.class)
public class TestConfig {

    @Autowired
    private TestStarterProperties testStarterProperties;

    @Bean
    public TestBean testBean() {
        return new TestBean(testStarterProperties.getMsg());
    }
}
