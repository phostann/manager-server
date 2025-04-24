package com.example.manager.utils;

/**
 * 用户上下文工具类
 */
public class UserContext {

    // use ThreadLocal to store user ID
    private static final ThreadLocal<Integer> userIdThreadLocal = new ThreadLocal<>();


    // set the user ID in ThreadLocal
    public static void setUserId(Integer userId) {
        userIdThreadLocal.set(userId);
    }

    // get the user ID from ThreadLocal
    public static Integer getUserId() {
        return userIdThreadLocal.get();
    }

    // clear the ThreadLocal variable
    public static void clear() {
        userIdThreadLocal.remove();
    }
} 