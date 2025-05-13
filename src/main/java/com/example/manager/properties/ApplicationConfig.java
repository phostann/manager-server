package com.example.manager.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "app")
public class ApplicationConfig {
    private String uploadPath;
}
