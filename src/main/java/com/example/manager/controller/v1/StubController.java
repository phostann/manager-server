package com.example.manager.controller.v1;

import com.example.manager.properties.ApplicationConfig;
import com.example.manager.service.ProxyService;
import com.example.manager.service.StubService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/stub")
@Slf4j
public class StubController {

    @Resource
    private ApplicationConfig appConfig;

    @Resource
    private StubService stubService;

    @Resource
    private ProxyService proxyService;

    /**
     * 初始化分片上传
     *
     * @return
     */
    @PostMapping("/upload/init")
    public void uploadStubInit(HttpServletRequest request, HttpServletResponse response) {
        proxyService.proxy(appConfig.getNodeRelayServer() + "/file/upload/init", request, response);
    }

    @PostMapping("/upload/chunk")
    public void uploadChunk(HttpServletRequest request, HttpServletResponse response) {
        proxyService.proxy(appConfig.getNodeRelayServer() + "/file/upload/chunk", request, response);
    }

    @PostMapping("/upload/complete")
    public void uploadComplete(HttpServletRequest request, HttpServletResponse response) {
        proxyService.proxy(appConfig.getNodeRelayServer() + "/file/upload/chunk", request, response);
    }

    @PostMapping("/upload")
    public void upload(HttpServletRequest request, HttpServletResponse response) {
        proxyService.proxy(appConfig.getNodeRelayServer() + "/file/upload", request, response);

    }

    @GetMapping("/download/init")
    public void downloadInit(HttpServletRequest request, HttpServletResponse response) {
        proxyService.proxy(appConfig.getNodeRelayServer() + "/file/download/init", request, response);
    }

    @GetMapping("/download/chunk")
    public void downloadChunk(HttpServletRequest request, HttpServletResponse response) {
        proxyService.proxy(appConfig.getNodeRelayServer() + "/file/download/chunk", request, response);
    }

    @GetMapping("/download/info")
    public void downloadInfo(HttpServletRequest request, HttpServletResponse response) {
        proxyService.proxy(appConfig.getNodeRelayServer() + "/file/download/info", request, response);
    }


    /**
     * 简单下载
     *
     * @param request
     * @param response
     */
    @GetMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        proxyService.proxy(appConfig.getNodeRelayServer() + "/file/download", request, response);
    }

}
