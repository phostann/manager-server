package com.example.manager.converter;

import com.example.manager.domain.dto.resource.ResourceCreateDTO;
import com.example.manager.entity.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ResourceConverter {
    ResourceConverter INSTANCE = Mappers.getMapper(ResourceConverter.class);

    Resource toEntity(ResourceCreateDTO dto);
}
