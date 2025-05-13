package com.example.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Socket消息格式
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocketMessage {
    /**
     * 消息类型
     */
    private String type;
    
    /**
     * 消息内容
     */
    private Object data;

    /**
     * 创建ping消息
     */
    public static SocketMessage ping() {
        return SocketMessage.builder()
                .type("ping")
                .build();
    }
    
    /**
     * 创建pong消息
     */
    public static SocketMessage pong() {
        return SocketMessage.builder()
                .type("pong")
                .build();
    }
} 