package com.example.manager.response;

import lombok.Data;

@Data
public class R<T> {
    private int code = 0;
    private String message = "success";
    private T data;

    /**
     * 成功响应，无数据
     */
    public static <T> R<T> ok() {
        R<T> r = new R<T>();
        r.setCode(ErrorCode.SUCCESS.getCode());
        r.setMessage(ErrorCode.SUCCESS.getMessage());
        return r;
    }

    /**
     * 成功响应，带数据
     */
    public static <T> R<T> ok(T data) {
        R<T> r = new R<T>();
        r.setCode(ErrorCode.SUCCESS.getCode());
        r.setMessage(ErrorCode.SUCCESS.getMessage());
        r.setData(data);
        return r;
    }

    /**
     * 成功响应，带自定义消息和数据
     */
    public static <T> R<T> ok(String message, T data) {
        R<T> r = new R<T>();
        r.setCode(ErrorCode.SUCCESS.getCode());
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    /**
     * 错误响应，使用预定义错误码
     */
    public static <T> R<T> error(ErrorCode errorCode) {
        R<T> r = new R<T>();
        r.setCode(errorCode.getCode());
        r.setMessage(errorCode.getMessage());
        return r;
    }

    /**
     * 错误响应，使用预定义错误码和数据
     */
    public static <T> R<T> error(ErrorCode errorCode, T data) {
        R<T> r = new R<T>();
        r.setCode(errorCode.getCode());
        r.setMessage(errorCode.getMessage());
        r.setData(data);
        return r;
    }

    /**
     * 错误响应，使用自定义消息
     */
    public static <T> R<T> error(String message) {
        R<T> r = new R<T>();
        r.setCode(ErrorCode.SYSTEM_ERROR.getCode());
        r.setMessage(message);
        return r;
    }

    /**
     * 错误响应，使用自定义错误码和消息
     */
    public static <T> R<T> error(int code, String message) {
        R<T> r = new R<T>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    /**
     * 错误响应，使用自定义错误码、消息和数据
     */
    public static <T> R<T> error(int code, String message, T data) {
        R<T> r = new R<T>();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }
}
