package com.example.manager.domain.dto.project;

import com.example.manager.domain.dto.common.PageDTO;
import com.example.manager.entity.ProjectResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectResourcePageDTO extends PageDTO<ProjectResource> {
    private String appName;
}
