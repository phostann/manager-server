package com.example.manager.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Socket消息类型枚举
 */
public enum MessageType {
    /**
     * 心跳请求
     */
    PING("ping"),

    /**
     * 心跳响应
     */
    PONG("pong"),

    /**
     * 路径消息
     */
    PATH("path"),

    /**
     * 普通消息
     */
    MESSAGE("message"),

    /**
     * 初始化节点
     */
    INIT_NODE("init_node"),

    /**
     * 初始化成功
     */
    INIT_NODE_SUCCESS("init_node_success"),

    /**
     * 初始化失败
     */
    INIT_NODE_FAILED("init_node_failed"),
    ;

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    /**
     * 获取消息类型的值，用于JSON序列化
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * 根据字符串值获取枚举
     */
    public static MessageType fromValue(String value) {
        for (MessageType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return MESSAGE; // 默认返回普通消息类型
    }
}