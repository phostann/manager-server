package com.example.manager.exception;

import com.example.manager.response.ErrorCode;
import lombok.Getter;

/**
 * Custom business exception for the application
 */
@Getter
public class BusinessException extends RuntimeException {
    private final int code;
    private final String message;

    public BusinessException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public BusinessException(ErrorCode errorCode, String message) {
        this.code = errorCode.getCode();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
} 