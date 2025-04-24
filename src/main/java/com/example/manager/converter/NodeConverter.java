package com.example.manager.converter;

import com.example.manager.domain.dto.node.NodeCreateDTO;
import com.example.manager.entity.Node;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NodeConverter {
    NodeConverter INSTANCE = Mappers.getMapper(NodeConverter.class);

    Node toEntity(NodeCreateDTO dto);
}
