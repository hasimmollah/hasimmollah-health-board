package com.hasim.healthboard.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
public class AppConfig {

    private String currentEnvironment;
    private String currentEnvironment1;
    public String getCurrentEnvironment() {
        return currentEnvironment;
    }
    public void setCurrentEnvironment(String currentEnvironment) {
        this.currentEnvironment = currentEnvironment;
    }
    public String getCurrentEnvironment1() {
        return currentEnvironment1;
    }
    public void setCurrentEnvironment1(String currentEnvironment1) {
        this.currentEnvironment1 = currentEnvironment1;
    }
}
