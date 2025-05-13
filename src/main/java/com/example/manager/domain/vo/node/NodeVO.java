package com.example.manager.domain.vo.node;

import lombok.Data;

@Data
public class NodeVO {
    private Integer id;
    private String name;
    private Integer status;
    private Boolean deployed;
}
