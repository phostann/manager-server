package com.example.manager.domain.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDto {

    @NotBlank(message = "原始密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String newPassword;

    @NotBlank(message = "确认密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String confirmPassword;
}
