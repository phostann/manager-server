package com.example.manager.interceptor;

import com.example.manager.exception.BusinessException;
import com.example.manager.properties.AuthConfigProperties;
import com.example.manager.response.ErrorCode;
import com.example.manager.utils.JwtUtils;
import com.example.manager.utils.UserContext;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private AuthConfigProperties authConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 从请求头中获取token
        final String authHeader = request.getHeader(authConfig.getJwtAuthHeader());
        final String token = jwtUtils.getTokenFromHeader(authHeader);

        // 排除 OPTIONS 请求
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        // 验证 token
        if (token == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "未提供认证token");
        }

        // 验证token是否有效
        if (!jwtUtils.validateToken(token)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "token已过期或无效");
        }

        // 验证是否是刷新token，拒绝使用刷新token访问接口
        if (jwtUtils.isRefreshToken(token)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "不能使用刷新token访问接口");
        }

        // 将用户ID放入请求属性中，方便后续使用
        Integer userId = jwtUtils.getUserId(token);
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "token中未包含用户ID");
        }
        // 设置用户ID到ThreadLocal中
        UserContext.setUserId(1);
        request.setAttribute("userId", 1);

        return true;
    }
} 