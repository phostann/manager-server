package com.example.manager.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.manager.domain.dto.project.ProjectResourcePageDTO;
import com.example.manager.entity.ProjectResource;
import com.example.manager.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author phos
 * @since 2025-04-17
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    Page<Resource> selectResourcesByProjectId(Page<ProjectResource> page, Integer projectId, ProjectResourcePageDTO dto);
}
