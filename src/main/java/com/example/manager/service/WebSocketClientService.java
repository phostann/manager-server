package com.example.manager.service;

import com.example.manager.config.SocketConfig;
import com.example.manager.domain.dto.socket.SocketMessage;
import com.example.manager.enums.MessageType;
import com.example.manager.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * WebSocket客户端服务
 * 如果Socket服务端支持WebSocket协议，可以使用此服务替代SocketClientService
 */
@Slf4j
@Service
public class WebSocketClientService {
    private final SocketConfig socketConfig;
    private WebSocketClient webSocketClient;
    private ScheduledExecutorService scheduledExecutorService;
    private final AtomicBoolean isConnected = new AtomicBoolean(false);
    private int reconnectAttempts = 0;

    public WebSocketClientService(SocketConfig socketConfig) {
        this.socketConfig = socketConfig;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(2);
    }

    /**
     * 获取完整的WebSocket地址
     */
    public String getFullAddress() {
        return socketConfig.getHost() + ":" + socketConfig.getPort() + socketConfig.getPath();
    }

    /**
     * 启动客户端
     */
    public void start() {
        connect();
    }

    /**
     * 建立连接
     */
    private void connect() {
        try {
            // 创建WebSocket URI，包含路径
            URI serverUri = createServerUri();
            log.info("正在连接WebSocket服务器: {}", serverUri);

            webSocketClient = new WebSocketClient(serverUri) {
                @Override
                public void onOpen(ServerHandshake handshakeData) {
                    log.info("成功连接到WebSocket服务器: {}", serverUri);
                    isConnected.set(true);
                    reconnectAttempts = 0;
                    // 连接成功后发送路径消息
                    sendPathMessage();
                    // 启动心跳机制
                    startHeartbeat();
                }

                @Override
                public void onMessage(String message) {
                    log.debug("收到消息: {}", message);
                    handleMessage(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("WebSocket连接关闭: code={}, reason={}, remote={}", code, reason, remote);
                    isConnected.set(false);

                    // 尝试重连
                    handleConnectionFailure(new Exception("WebSocket连接关闭"));
                }

                @Override
                public void onError(Exception ex) {
                    log.error("WebSocket连接出错: {}", ex.getMessage(), ex);
                    isConnected.set(false);
                }
            };

            // 连接到服务器
            webSocketClient.connect();

        } catch (URISyntaxException e) {
            log.error("创建WebSocket URI出错: {}", e.getMessage(), e);
        }
    }

    /**
     * 发送路径消息
     */
    private void sendPathMessage() {
        Map<String, String> pathData = new HashMap<>();
        pathData.put("path", socketConfig.getPath());

        SocketMessage pathMessage = SocketMessage.builder()
                .type(MessageType.PATH)
                .data(pathData)
                .build();

        sendMessage(pathMessage);
        log.info("已发送路径消息: {}", socketConfig.getPath());
    }

    /**
     * 创建服务器URI
     */
    private URI createServerUri() throws URISyntaxException {
        // 使用ws或wss协议
        String protocol = "ws";
        String uri = String.format("%s://%s:%d%s",
                protocol, socketConfig.getHost(), socketConfig.getPort(), socketConfig.getPath());
        return new URI(uri);
    }

    /**
     * 处理连接失败
     */
    private void handleConnectionFailure(Exception e) {
        if (isConnected.get()) {
            return;
        }

        log.error("WebSocket连接失败: {}", e.getMessage());

        // 尝试重连
        if (reconnectAttempts < socketConfig.getMaxReconnectAttempts()) {
            reconnectAttempts++;
            log.info("尝试第 {} 次重连, {} 毫秒后重试", reconnectAttempts, socketConfig.getReconnectInterval());

            scheduledExecutorService.schedule(this::connect,
                    socketConfig.getReconnectInterval(), TimeUnit.MILLISECONDS);
        } else {
            log.error("达到最大重连次数 {}, 停止重连", socketConfig.getMaxReconnectAttempts());
        }
    }

    /**
     * 处理接收到的消息
     */
    private void handleMessage(String messageJson) {
        try {
            SocketMessage message = JsonUtils.fromJson(messageJson, SocketMessage.class);
            if (message != null) {
                switch (message.getType()) {
                    case PING:
                        // 收到ping，回复pong
                        sendMessage(SocketMessage.builder()
                                .type(MessageType.PONG)
                                .build());
                        break;
                    case PONG:
                        // 收到pong，心跳确认
                        log.debug("收到服务器心跳响应");
                        break;
                    case PATH:
                        // 处理路径消息
                        log.info("收到路径消息: {}", message.getData());
                        break;
                    case INIT_NODE:
                        // 处理初始化节点消息
                        log.info("收到初始化节点消息: {}", message.getData());
                        break;
                    default:
                        // 处理其他类型消息
                        log.info("收到消息: type={}, data={}", message.getType(), message.getData());
                }
            }
        } catch (Exception e) {
            log.error("处理消息出错: {}", e.getMessage(), e);
        }
    }

    /**
     * 启动心跳机制
     */
    private void startHeartbeat() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (isConnected.get()) {
                sendMessage(SocketMessage.builder()
                        .type(MessageType.PING)
                        .build());
            }
        }, 0, socketConfig.getHeartbeatInterval(), TimeUnit.MILLISECONDS);
    }

    /**
     * 发送消息
     */
    public void sendMessage(SocketMessage message) {
        if (!isConnected.get() || webSocketClient == null) {
            log.warn("WebSocket未连接，无法发送消息");
            return;
        }

        try {
            String messageJson = JsonUtils.toJson(message);
            log.debug("发送消息: {}", messageJson);
            webSocketClient.send(messageJson);
        } catch (Exception e) {
            log.error("发送消息失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 关闭连接
     */
    public void closeConnection() {
        if (webSocketClient != null) {
            try {
                webSocketClient.close();
                log.info("WebSocket连接已关闭");
            } catch (Exception e) {
                log.error("关闭WebSocket连接出错: {}", e.getMessage(), e);
            }
        }
        isConnected.set(false);
    }

    /**
     * 停止服务
     */
    public void stop() {
        closeConnection();
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
    }
} 