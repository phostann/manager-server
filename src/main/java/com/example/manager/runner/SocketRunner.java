package com.example.manager.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.manager.service.WebSocketClientService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SocketRunner implements CommandLineRunner {

    // 注释掉标准Socket客户端
    // @Resource
    // private SocketClientService socketClientService;
    
    /**
     * 使用WebSocket，可以直接在连接URL中包含路径
     */
    @Resource
    private WebSocketClientService webSocketClientService;
    
    // 启用WebSocket
    private final boolean useWebSocket = true;

    @Override
    public void run(String... args) {
        log.info("Socket客户端正在启动...");
        
        if (useWebSocket) {
            // 使用WebSocket客户端
            webSocketClientService.start();
            log.info("WebSocket客户端已启用，连接路径: {}", "ws://" + webSocketClientService.getFullAddress());
        } else {
            // 使用标准Socket客户端
            // socketClientService.start();
            log.info("标准Socket客户端已禁用");
        }
    }
}
