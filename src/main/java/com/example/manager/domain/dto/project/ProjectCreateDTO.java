package com.example.manager.domain.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectCreateDTO {
    @NotBlank(message = "项目名称不能为空")
    private String name;
}
