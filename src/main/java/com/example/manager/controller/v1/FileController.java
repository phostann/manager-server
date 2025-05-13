package com.example.manager.controller.v1;

import com.example.manager.exception.BusinessException;
import com.example.manager.properties.ApplicationConfig;
import com.example.manager.response.ErrorCode;
import com.example.manager.response.R;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/v1/file")
@Slf4j
public class FileController {

    @Resource
    private ApplicationConfig appConfig;

    // upload
    @PostMapping("/upload")
    public R<Void> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return R.error(ErrorCode.FILE_UPLOAD_FAILED.getCode(), "文件为空");
        }

        try {
            // 获取安全的文件名
            String originalFilename = file.getOriginalFilename();

            // 创建目录
            Path uploadDir = Paths.get(appConfig.getUploadPath());
            Files.createDirectories(uploadDir);

            // 保存文件
            Path destPath = uploadDir.resolve(originalFilename);
            Files.copy(file.getInputStream(), destPath, StandardCopyOption.REPLACE_EXISTING);

            return R.ok();
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            return R.error(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    // download
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 参数验证
        if (fileName == null || fileName.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
        }

        try {
            // 规范化文件路径并进行安全检查
            Path uploadDir = Paths.get(appConfig.getUploadPath()).toAbsolutePath().normalize();
            Path filePath = uploadDir.resolve(fileName).normalize();

            // 安全检查：确保请求的文件在上传目录内
            if (!filePath.startsWith(uploadDir)) {
                log.warn("检测到路径遍历攻击尝试: {}", fileName);
                throw new BusinessException(ErrorCode.FILE_DOWNLOAD_FAILED);
            }

            // 检查文件是否存在
            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
            }

            // 获取MIME类型
            String contentType = determineContentType(filePath);

            // 设置响应头
            String safeFileName = new File(fileName).getName(); // 只使用文件名部分
            response.setHeader("Content-Disposition", "attachment;filename=" + safeFileName);
            response.setContentType(contentType);
            response.setContentLengthLong(Files.size(filePath));

            // 使用NIO直接复制文件到输出流
            Files.copy(filePath, response.getOutputStream());
            response.getOutputStream().flush();

        } catch (IOException e) {
            log.error("文件下载失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.FILE_DOWNLOAD_FAILED);
        }
    }

    /**
     * 确定文件的内容类型
     */
    private String determineContentType(Path filePath) {
        try {
            return Files.probeContentType(filePath);
        } catch (IOException e) {
            log.warn("无法确定文件类型: {}", e.getMessage());
            return "application/octet-stream";
        }
    }
}
