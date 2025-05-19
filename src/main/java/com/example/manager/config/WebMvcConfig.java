package com.example.manager.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.manager.domain.dto.common.PageDTO;
import com.example.manager.interceptor.JwtInterceptor;
import com.example.manager.properties.AuthConfigProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Resource
    private AuthConfigProperties authConfig;

    @Resource
    private ObjectMapper objectMapper;

    public WebMvcConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 所有请求都需要经过拦截器
                .excludePathPatterns(authConfig.getWhiteList()); // 排除登录、注册、刷新token接口
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new QueryParameterResolver(objectMapper));
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    public static class QueryParameterResolver implements HandlerMethodArgumentResolver {
        private final ObjectMapper objectMapper;

        public QueryParameterResolver(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            // 只有PageDTO类型的参数才支持
            return PageDTO.class.isAssignableFrom(parameter.getParameterType());
        }

        @Override
        public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            if (request != null) {
                HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
                if (nativeRequest == null) {
                    return null;
                }

                Map<String, String[]> parameterMap = nativeRequest.getParameterMap();

                if (parameterMap == null) {
                    return null;
                }

                Map<String, String> map = parameterMap.entrySet()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        m -> String.join(",", Arrays.asList(m.getValue()))));

                Class<?> parameterType = parameter.getParameterType();

                return objectMapper.convertValue(map, parameterType);
            }
            return null;
        }
    }
}