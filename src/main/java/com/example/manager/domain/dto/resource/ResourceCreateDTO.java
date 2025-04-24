package com.example.manager.domain.dto.resource;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResourceCreateDTO {

    @NotBlank(message = "资源名称不能为空")
    private String name;

    @NotBlank(message = "业务名称不能为空")
    private String appName;

    @NotBlank(message = "资源描述不能为空")
    private String description;
}