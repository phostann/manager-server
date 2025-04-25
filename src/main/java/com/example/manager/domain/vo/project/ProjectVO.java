package com.example.manager.domain.vo.project;

import com.example.manager.entity.Node;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectVO {
    private Integer id;
    private String name;

    private Integer nodeCount;
    private Integer resourceCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
