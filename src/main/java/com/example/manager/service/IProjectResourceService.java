package com.example.manager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.manager.domain.dto.project.ProjectResourcePageDTO;
import com.example.manager.entity.ProjectResource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.manager.entity.Resource;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author phos
 * @since 2025-04-17
 */
public interface IProjectResourceService extends IService<ProjectResource> {
    boolean bindResources(Integer projectId, List<Integer> resourceIds);

    boolean unbindResources(Integer projectId, List<Integer> resourceIds);

    Page<ProjectResource> selectResourcesByPage(Integer projectId, ProjectResourcePageDTO dto);
}
