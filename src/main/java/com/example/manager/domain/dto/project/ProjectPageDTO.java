package com.example.manager.domain.dto.project;

import com.example.manager.domain.dto.common.PageDTO;
import com.example.manager.entity.Project;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectPageDTO extends PageDTO<Project> {
    private String name;
} 