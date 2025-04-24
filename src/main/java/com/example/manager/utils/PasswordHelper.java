package com.example.manager.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密工具类
 */
public class PasswordHelper {
    // BCrypt强哈希函数加密
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    /**
     * 对密码进行加密
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        return encoder.encode(password);
    }
    
    /**
     * 验证密码是否匹配
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
    
    /**
     * 判断密码是否需要升级（可用于升级旧的加密算法）
     * @param encodedPassword 加密后的密码
     * @return 是否需要升级
     */
    public static boolean upgradeEncoding(String encodedPassword) {
        return encoder.upgradeEncoding(encodedPassword);
    }
}
