package com.example.manager.utils;

import com.example.manager.properties.AuthConfigProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT工具类
 */
@Component
public class JwtUtils {
    
    private final AuthConfigProperties authConfig;
    
    public JwtUtils(AuthConfigProperties authConfig) {
        this.authConfig = authConfig;
    }
    
    /**
     * 生成访问token
     *
     * @param userId 用户ID
     * @return token字符串
     */
    public String generateAccessToken(Integer userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(authConfig.getExpirationDate())
                .signWith(authConfig.getSecretKey())
                .compact();
    }
    
    /**
     * 生成刷新token
     *
     * @param userId 用户ID
     * @return 刷新token字符串
     */
    public String generateRefreshToken(Integer userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("refresh", true);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(authConfig.getRefreshExpirationDate())
                .signWith(authConfig.getSecretKey())
                .compact();
    }
    
    /**
     * 从token中获取Claims
     *
     * @param token token字符串
     * @return Claims对象
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(authConfig.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 从token中获取用户ID
     *
     * @param token token字符串
     * @return 用户ID
     */
    public Integer getUserId(String token) {
        return Integer.valueOf(extractClaim(token, Claims::getSubject));
    }
    
    /**
     * 从token中提取特定数据
     *
     * @param token token字符串
     * @param claimsResolver 提取函数
     * @param <T> 返回类型
     * @return 提取的数据
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * 判断token是否是刷新token
     *
     * @param token token字符串
     * @return 是否是刷新token
     */
    public boolean isRefreshToken(String token) {
        try {
            final Claims claims = extractAllClaims(token);
            return claims.get("refresh", Boolean.class) != null && 
                   claims.get("refresh", Boolean.class);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 验证token是否有效
     *
     * @param token token字符串
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            final Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * 从请求头获取token
     *
     * @param authHeader 认证请求头
     * @return token字符串，如果没有则返回null
     */
    public String getTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith(authConfig.getTokenPrefix())) {
            return authHeader.substring(authConfig.getTokenPrefix().length());
        }
        return null;
    }
} 