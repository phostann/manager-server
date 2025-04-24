package com.example.manager.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 强密码校验器实现
 */
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    
    // 密码必须包含大写字母、小写字母、数字和特殊字符
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9]");
    
    @Override
    public void initialize(StrongPassword constraintAnnotation) {
        // 初始化，这里不需要做任何事
    }
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // 如果密码为空，则由@NotBlank或@NotNull处理，这里返回true
        if (password == null || password.isEmpty()) {
            return true;
        }
        
        // 检查密码是否满足所有条件
        boolean hasUppercase = UPPERCASE_PATTERN.matcher(password).find();
        boolean hasLowercase = LOWERCASE_PATTERN.matcher(password).find();
        boolean hasDigit = DIGIT_PATTERN.matcher(password).find();
        boolean hasSpecialChar = SPECIAL_CHAR_PATTERN.matcher(password).find();
        
        // 如果有一个条件不满足，构建自定义错误信息
        if (!hasUppercase || !hasLowercase || !hasDigit || !hasSpecialChar) {
            context.disableDefaultConstraintViolation();
            StringBuilder message = new StringBuilder("密码必须包含: ");
            
            if (!hasUppercase) message.append("大写字母, ");
            if (!hasLowercase) message.append("小写字母, ");
            if (!hasDigit) message.append("数字, ");
            if (!hasSpecialChar) message.append("特殊字符, ");
            
            // 移除最后的逗号和空格
            String customMessage = message.substring(0, message.length() - 2);
            
            context.buildConstraintViolationWithTemplate(customMessage)
                   .addConstraintViolation();
                   
            return false;
        }
        
        return true;
    }
} 