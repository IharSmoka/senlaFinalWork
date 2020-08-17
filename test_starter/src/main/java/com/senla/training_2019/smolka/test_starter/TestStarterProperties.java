package com.senla.training_2019.smolka.test_starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("test_starter")
public class TestStarterProperties {
    private String msg = "default";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
