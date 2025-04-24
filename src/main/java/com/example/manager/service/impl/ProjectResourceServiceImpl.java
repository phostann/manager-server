package com.example.manager.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.manager.domain.dto.project.ProjectResourcePageDTO;
import com.example.manager.entity.ProjectResource;
import com.example.manager.mapper.ProjectResourceMapper;
import com.example.manager.service.IProjectResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author phos
 * @since 2025-04-17
 */
@Service
public class ProjectResourceServiceImpl extends ServiceImpl<ProjectResourceMapper, ProjectResource> implements IProjectResourceService {
    @Transactional
    @Override
    public boolean bindResources(Integer projectId, List<Integer> resourceIds) {
        // 查询已经绑定过的资源
        List<ProjectResource> list = this.lambdaQuery()
                .eq(ProjectResource::getProjectId, projectId)
                .in(ProjectResource::getResourceId, resourceIds)
                .list();
        // 过滤掉已经绑定的资源
        List<Integer> unboundResourceIds = resourceIds.stream()
                .filter(resourceId -> list.stream().noneMatch(pr -> pr.getResourceId().equals(resourceId)))
                .toList();
        // 批量插入未绑定的资源
        if (!unboundResourceIds.isEmpty()) {
            List<ProjectResource> projectResources = unboundResourceIds.stream()
                    .map(resourceId -> {
                        ProjectResource projectResource = new ProjectResource();
                        projectResource.setProjectId(projectId);
                        projectResource.setResourceId(resourceId);
                        projectResource.setVersion("0.0.1");
                        return projectResource;
                    })
                    .toList();
            return this.saveBatch(projectResources);
        }
        return false;
    }

    @Override
    public boolean unbindResources(Integer projectId, List<Integer> resourceIds) {
        return this.lambdaUpdate()
                .eq(ProjectResource::getProjectId, projectId)
                .in(ProjectResource::getResourceId, resourceIds)
                .remove();
    }

    @Override
    public Page<ProjectResource> selectResourcesByPage(Integer projectId, ProjectResourcePageDTO dto) {


        return this.lambdaQuery()
                .eq(ProjectResource::getProjectId, projectId)
                .page(dto.toMybatisPage());
    }
}
