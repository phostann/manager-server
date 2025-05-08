package com.example.manager.domain.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectUpdateDTO {
    private String name;
    private String config;
}
