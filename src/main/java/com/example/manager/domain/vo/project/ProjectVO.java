package com.example.manager.domain.vo.project;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectVO {
    private Integer id;
    private String uid;
    private String name;

    private Integer nodeCount;
    private Integer resourceCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
