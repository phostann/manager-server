package com.example.manager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "socket")
public class SocketConfig {
    /**
     * Socket服务器地址
     */
    private String host = "localhost";
    
    /**
     * Socket服务器端口
     */
    private int port = 8080;
    
    /**
     * Socket路径
     */
    private String path = "/socket/node/";
    
    /**
     * 重连间隔时间(毫秒)
     */
    private long reconnectInterval = 5000;
    
    /**
     * 最大重连次数
     */
    private int maxReconnectAttempts = 10;
    
    /**
     * 心跳间隔时间(毫秒)
     */
    private long heartbeatInterval = 30000;
} 