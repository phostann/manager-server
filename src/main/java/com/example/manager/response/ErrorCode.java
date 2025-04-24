package com.example.manager.response;

import lombok.Getter;

/**
 * Unified error codes for the application
 * 
 * Error code structure:
 * - 1000-1999: Common system errors
 * - 2000-2999: Business/domain errors
 * - 3000-3999: Data/validation errors
 * - 4000-4999: Integration/external service errors
 * - 5000-5999: Security/authentication errors
 * - 9000-9999: Other/misc errors
 */
@Getter
public enum ErrorCode {
    // Common success response
    SUCCESS(0, "成功"),
    
    // 1000-1999: Common system errors
    SYSTEM_ERROR(1000, "系统错误"),
    PARAMETER_ERROR(1001, "参数错误"),
    DATA_NOT_FOUND(1002, "数据不存在"),
    REQUEST_TIMEOUT(1005, "请求超时"),

    // 2000-2999: Business/domain errors
    BUSINESS_ERROR(2000, "业务错误"),
    
    // 2100-2199: User domain errors
    USER_NOT_FOUND(2100, "用户不存在"),
    USER_EXISTS(2101, "用户已存在"),
    EMAIL_ALREADY_EXISTS(2102, "邮箱已存在"),
    USER_PASSWORD_ERROR(2103, "用户密码错误"),
    USER_LOGIN_ERROR(2104, "用户登录失败"),
    USER_LOGOUT_ERROR(2105, "用户登出失败"),
    
    // 2200-2299: Project domain errors
    PROJECT_EXISTS(2200, "项目已存在"),
    PROJECT_CREATE_FAILED(2201, "项目创建失败"),
    PROJECT_NOT_FOUND(2202, "项目不存在"),
    PROJECT_UPDATE_FAILED(2203, "项目更新失败"),
    PROJECT_DELETE_FAILED(2204, "项目删除失败"),
    PROJECT_NODE_MISMATCH(2205, "项目成员不存在"),
    PROJECT_NODE_UNBIND_FAILED(2206, "项目成员解绑失败"),
    PROJECT_NODE_BIND_FAILED(2207, "节点绑定失败"),
    PROJECT_RESOURCE_BIND_FAILED(2208, "资源绑定失败"),
    PROJECT_RESOURCE_UNBIND_FAILED(2209, "资源解绑失败"),

    // 2300-2399: Resource domain errors
    RESOURCE_EXISTS(2301, "资源已存在"),
    RESOURCE_NOT_FOUND(2302, "资源不存在"),
    RESOURCE_CREATE_FAILED(2303, "资源创建失败"),
    RESOURCE_UPDATE_FAILED(2304, "资源更新失败"),
    RESOURCE_DELETE_FAILED(2305, "资源删除失败"),

    // 2400-2499: Node domain errors
    NODE_EXISTS(2400, "节点已存在"),
    NODE_CREATE_FAILED(2401, "节点创建失败"),
    NODE_NOT_FOUND(2402, "节点不存在"),
    NODE_UPDATE_FAILED(2403, "节点更新失败"),
    NODE_DELETE_FAILED(2404, "节点删除失败"),
    NODE_BOUNDED(2405, "节点已绑定"),

    // 4000-4999: Integration/external service errors
    INTEGRATION_ERROR(4000, "集成错误"),
    DATABASE_ERROR(4001, "数据库错误"),
    REMOTE_SERVICE_ERROR(4002, "远程服务错误"),
    
    // 5000-5999: Security/authentication errors
    UNAUTHORIZED(5000, "未授权"),
    FORBIDDEN(5001, "禁止访问"),
    PASSWORD_NOT_MATCH(5002, "两次输入的密码不一致");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}