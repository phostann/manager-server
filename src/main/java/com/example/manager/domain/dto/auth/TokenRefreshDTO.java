package com.example.manager.domain.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 刷新Token DTO
 */
@Data
public class TokenRefreshDTO {
    /**
     * 刷新token
     */
    @NotBlank(message = "刷新token不能为空")
    private String refreshToken;
} 