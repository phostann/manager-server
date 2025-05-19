package com.example.manager.service;

import com.example.manager.domain.dto.stub.UploadStubInitDTO;
import com.example.manager.exception.BusinessException;
import com.example.manager.properties.ApplicationConfig;
import com.example.manager.response.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
public class StubService {

    @Resource
    private ApplicationConfig applicationConfig;

    @Resource
    private ObjectMapper objectMapper;

    public Map<String, Object> uploadInit(Map<String, Object> params) {
        // 将请求转发至文件中转服务 http://localhost:8080/file/upload/init 请求体为 {file_name: "",
        // file_size: 0, chunk_size: 0, file_hash: ""}
        String url = applicationConfig.getNodeRelayServer() + "/file/upload/init";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                return objectMapper.readValue(response.getBody().getBytes(), new TypeReference<Map<String, Object>>() {
                });
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED, e.getMessage());
            }
        } else {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public Map<String, Object> uploadChunk(Map<String, Object> params) {
        String url = applicationConfig.getNodeRelayServer() + "/file/upload/chunk";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                return objectMapper.readValue(response.getBody().getBytes(), new TypeReference<Map<String, Object>>() {
                });
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED, e.getMessage());
            }
        } else {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public Map<String, Object> uploadComplete(Map<String, Object> params) {
        String url = applicationConfig.getNodeRelayServer() + "/file/upload/complete";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                return objectMapper.readValue(response.getBody().getBytes(), new TypeReference<Map<String, Object>>() {
                });
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED, e.getMessage());
            }
        } else {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public Map<String, Object> upload(MultiValueMap<String, Object> params) {
        String url = applicationConfig.getNodeRelayServer() + "/file/upload";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                return objectMapper.readValue(response.getBody().getBytes(), new TypeReference<Map<String, Object>>() {
                });
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED, e.getMessage());
            }
        } else {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }
}
