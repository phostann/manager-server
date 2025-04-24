package com.example.manager.domain.dto.node;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NodeCreateDTO {
    @NotBlank(message = "节点名称不能为空")
    private String name;
}
