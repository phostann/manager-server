package com.example.manager.domain.dto.resource;

import com.example.manager.domain.dto.common.PageDTO;
import com.example.manager.entity.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResourceQueryDTO extends PageDTO<Resource> {

    /**
     * 资源名称
     */
    private String name;

    /**
     * 业务名称
     */
    private String appName;

    /**
     * 资源版本
     */
    private String version;
}