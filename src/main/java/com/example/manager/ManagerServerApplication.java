package com.example.manager;

import com.example.manager.properties.AuthConfigProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {AuthConfigProperties.class})
public class ManagerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerServerApplication.class, args);
    }

}
