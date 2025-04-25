package com.example.manager.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.manager.domain.dto.project.ProjectPageDTO;
import com.example.manager.domain.vo.project.ProjectVO;
import com.example.manager.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author phos
 * @since 2025-04-17
 */
public interface ProjectMapper extends BaseMapper<Project> {
    Page<ProjectVO> selectProjects(Page<Project> page, ProjectPageDTO dto);
}
