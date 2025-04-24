package com.example.manager.domain.vo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token返回VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO {
    /**
     * 访问token
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 过期时间（毫秒）
     */
    private long expireIn;

    /**
     * token类型
     */
    private String tokenType;
} 