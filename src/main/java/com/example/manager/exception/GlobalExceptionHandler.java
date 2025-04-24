package com.example.manager.exception;

import com.example.manager.response.ErrorCode;
import com.example.manager.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global exception handler for the application
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle custom business exceptions
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public <T> R<T> handleBusinessException(BusinessException e) {
        log.error("Business exception: {}", e.getMessage(), e);
        return R.error(e.getCode(), e.getMessage());
    }
    
    /**
     * Handle validation exceptions from @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public R<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("Parameter validation failed: {}", message);
        return R.error(ErrorCode.PARAMETER_ERROR.getCode(), message);
    }
    
    /**
     * Handle validation exceptions from form binding
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public R<Object> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("Parameter binding failed: {}", message);
        return R.error(ErrorCode.PARAMETER_ERROR.getCode(), message);
    }
    
    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public R<Object> handleException(Exception e) {
        log.error("Unexpected error: ", e);
        return R.error(ErrorCode.SYSTEM_ERROR.getCode(), "System error: " + e.getMessage());
    }
} 