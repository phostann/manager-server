package com.example.manager.service;

import com.example.manager.config.SocketConfig;
import com.example.manager.domain.SocketMessage;
import com.example.manager.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class SocketClientService {
    private final SocketConfig socketConfig;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private ScheduledExecutorService scheduledExecutorService;
    private final AtomicBoolean isConnected = new AtomicBoolean(false);
    private int reconnectAttempts = 0;

    public SocketClientService(SocketConfig socketConfig) {
        this.socketConfig = socketConfig;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(2);
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
            log.info("正在连接Socket服务器: {}:{}{}", socketConfig.getHost(), socketConfig.getPort(), socketConfig.getPath());
            // 连接到Socket服务器
            socket = new Socket(socketConfig.getHost(), socketConfig.getPort());
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            isConnected.set(true);
            reconnectAttempts = 0;
            log.info("成功连接到Socket服务器");
            
            // 发送路径消息
            sendPathMessage();
            
            // 启动接收消息线程
            startReceivingMessages();
            // 启动心跳机制
            startHeartbeat();
        } catch (IOException e) {
            handleConnectionFailure(e);
        }
    }
    
    /**
     * 发送路径消息
     */
    private void sendPathMessage() {
        Map<String, String> pathData = new HashMap<>();
        pathData.put("path", socketConfig.getPath());
        
        SocketMessage pathMessage = SocketMessage.builder()
                .type("path")
                .data(pathData)
                .build();
        
        sendMessage(pathMessage);
        log.info("已发送路径消息: {}", socketConfig.getPath());
    }

    /**
     * 处理连接失败
     */
    private void handleConnectionFailure(Exception e) {
        log.error("Socket连接失败: {}", e.getMessage());
        isConnected.set(false);
        
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
     * 启动消息接收线程
     */
    private void startReceivingMessages() {
        Thread receiverThread = new Thread(() -> {
            try {
                String message;
                while (isConnected.get() && (message = reader.readLine()) != null) {
                    log.debug("收到消息: {}", message);
                    handleMessage(message);
                }
            } catch (IOException e) {
                log.error("读取消息出错: {}", e.getMessage());
                closeConnection();
                // 如果连接断开，尝试重连
                handleConnectionFailure(e);
            }
        });
        receiverThread.setDaemon(true);
        receiverThread.start();
    }

    /**
     * 处理接收到的消息
     */
    private void handleMessage(String messageJson) {
        try {
            SocketMessage message = JsonUtils.fromJson(messageJson, SocketMessage.class);
            if (message != null) {
                switch (message.getType()) {
                    case "ping":
                        // 收到ping，回复pong
                        sendMessage(SocketMessage.pong());
                        break;
                    case "pong":
                        // 收到pong，心跳确认
                        log.debug("收到服务器心跳响应");
                        break;
                    default:
                        // 处理其他类型消息
                        log.info("收到未处理的消息类型: {}", message.getType());
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
                sendMessage(SocketMessage.ping());
            }
        }, 0, socketConfig.getHeartbeatInterval(), TimeUnit.MILLISECONDS);
    }

    /**
     * 发送消息
     */
    public void sendMessage(SocketMessage message) {
        if (!isConnected.get()) {
            log.warn("Socket未连接，无法发送消息");
            return;
        }
        
        try {
            String messageJson = JsonUtils.toJson(message);
            log.debug("发送消息: {}", messageJson);
            writer.println(messageJson);
        } catch (Exception e) {
            log.error("发送消息失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 关闭连接
     */
    public void closeConnection() {
        isConnected.set(false);
        
        try {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            log.info("Socket连接已关闭");
        } catch (IOException e) {
            log.error("关闭Socket连接出错: {}", e.getMessage(), e);
        }
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