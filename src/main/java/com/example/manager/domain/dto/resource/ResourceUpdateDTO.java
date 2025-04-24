package com.example.manager.domain.dto.resource;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ResourceUpdateDTO {

    /**
     * 资源名称
     */
    private String name;

    /**
     * 业务名称
     */
    private String appName;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 资源版本
     */
    @Min(1)
    @Max(3)
    private Integer updateType;
} 