package com.example.manager.domain.dto.node;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class NodeUpdateDTO {
    private String name;

    @Min(0)
    @Max(1)
    private Integer status;
}
