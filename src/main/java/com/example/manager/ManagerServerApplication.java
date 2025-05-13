package com.example.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.manager.properties.ApplicationConfig;
import com.example.manager.properties.AuthConfigProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {AuthConfigProperties.class, ApplicationConfig.class})
public class ManagerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerServerApplication.class, args);
    }

}
