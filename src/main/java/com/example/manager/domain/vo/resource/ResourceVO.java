package com.example.manager.domain.vo.resource;

import lombok.Data;

/**
 * 资源VO
 */
@Data
public class ResourceVO {
    /**
     * 资源ID
     */
    private Integer id;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 版本号
     */
    private String version;

    /**
     * 创建时间
     */
    private Long createdAt;

    /**
     * 更新时间
     */
    private Long updatedAt;
} 