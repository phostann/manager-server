package com.example.manager.service;

import com.example.manager.exception.BusinessException;
import com.example.manager.response.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

@Service
public class ProxyService {

    public void proxy(String url, HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpEntity<?> httpEntity = constructHttpEntity(request);
            // 发起请求
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                String[] values = request.getParameterValues(name);
                for (String value : values) {
                    builder.queryParam(name, value);
                }
            }

            String finalUrl = builder.build().toUriString();

            ResponseEntity<byte[]> resp = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.valueOf(request.getMethod()),
                    httpEntity,
                    byte[].class
            );

            // 拷贝响应头 状态码 等
            response.setStatus(resp.getStatusCode().value());
            resp.getHeaders().forEach((key, value) -> response.setHeader(key, String.join(",", value)));

            byte[] body = resp.getBody();
            if (body == null || body.length == 0) {
                response.getOutputStream().flush();
            } else {
                response.getOutputStream().write(body);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.PROXY_ERROR, e.getMessage());
        }
    }

    private HttpEntity<?> constructHttpEntity(HttpServletRequest request) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        // 拷贝请求头
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            headers.add(headerName, request.getHeader(headerName));
        });

        // 判断是否是 multipart/form-data
        boolean isMultipart = request.getContentType() != null && request.getContentType().contains(MediaType.MULTIPART_FORM_DATA_VALUE);
        HttpEntity<?> httpEntity;

        if (isMultipart && request instanceof MultipartHttpServletRequest multipartRequest) {
            // 处理文件和表单字段
            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            // 普通字段
            multipartRequest.getParameterMap().forEach((k, v) -> body.add(k, v.length == 1 ? v[0] : Arrays.asList(v)));

            // 修复：正确处理文件字段
            multipartRequest.getFileMap().forEach((name, file) -> {
                try {
                    ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename();
                        }
                    };
                    body.add(name, resource);
                } catch (IOException e) {
                    throw new BusinessException(ErrorCode.PROXY_ERROR, "处理上传文件失败: " + e.getMessage());
                }
            });

            // 移除  Content-Length 头, 因为 RestTemplate 会自动计算
            headers.remove(HttpHeaders.CONTENT_LENGTH);
            httpEntity = new HttpEntity<>(body, headers);
        } else {
            // 普通表单或 JSON
            byte[] bodyBytes = request.getInputStream().readAllBytes();
            httpEntity = new HttpEntity<>(bodyBytes, headers);
        }

        return httpEntity;
    }
}
