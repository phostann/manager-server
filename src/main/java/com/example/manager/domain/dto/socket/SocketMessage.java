package com.example.manager.domain.dto.socket;

import com.example.manager.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Socket消息格式，用于客户端和服务器之间传输数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocketMessage {
    /**
     * 消息类型
     */
    private MessageType type;
    
    /**
     * 消息内容
     */
    private Object data;

    /**
     * 创建ping消息
     */
    public static SocketMessage ping() {
        return SocketMessage.builder()
                .type(MessageType.PING)
                .build();
    }
    
    /**
     * 创建pong消息
     */
    public static SocketMessage pong() {
        return SocketMessage.builder()
                .type(MessageType.PONG)
                .build();
    }
}