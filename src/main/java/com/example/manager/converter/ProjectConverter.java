package com.example.manager.converter;

import com.example.manager.domain.vo.project.ProjectVO;
import com.example.manager.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectConverter {
    ProjectConverter INSTANCE = Mappers.getMapper(ProjectConverter.class);

    ProjectVO entityToVO(Project project);
}
