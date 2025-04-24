package com.example.manager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 强密码校验注解
 * 要求密码包含：大写字母、小写字母、数字和特殊字符
 */
@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "密码必须包含大写字母、小写字母、数字和特殊字符";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 