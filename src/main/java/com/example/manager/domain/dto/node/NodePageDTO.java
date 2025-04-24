package com.example.manager.domain.dto.node;

import com.example.manager.domain.dto.common.PageDTO;
import com.example.manager.entity.Node;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NodePageDTO extends PageDTO<Node> {
    private String name;

    @Min(0)
    @Max(1)
    private Integer status;
}
