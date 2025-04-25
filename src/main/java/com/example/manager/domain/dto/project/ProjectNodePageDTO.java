package com.example.manager.domain.dto.project;

import com.example.manager.domain.dto.common.PageDTO;
import com.example.manager.entity.Node;
import com.example.manager.entity.ProjectResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectNodePageDTO extends PageDTO<Node> {
    private String name;
}
