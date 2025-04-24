package com.example.manager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.manager.domain.dto.project.ProjectCreateDTO;
import com.example.manager.domain.dto.project.ProjectPageDTO;
import com.example.manager.domain.dto.project.ProjectResourcePageDTO;
import com.example.manager.domain.dto.project.ProjectUpdateDTO;
import com.example.manager.domain.vo.project.ProjectVO;
import com.example.manager.entity.Project;
import com.example.manager.entity.Resource;
import jakarta.validation.Valid;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author phos
 * @since 2025-04-17
 */
public interface IProjectService extends IService<Project> {

    void createProject(@Valid ProjectCreateDTO dto);

    void updateProject(Integer id, @Valid ProjectUpdateDTO dto);

    Page<ProjectVO> selectProjectByPage(@Valid ProjectPageDTO dto);

    Project selectProjectById(Integer id);

    void deleteProject(Integer id);

    void bindNode(Integer id, Integer nodeId);

    void unbindNode(Integer projectId, Integer nodeId);

    void bindResources(Integer projectId, List<Integer> resourceIds);

    void unbindResources(Integer projectId, List<Integer> resourceIds);

    Page<Resource> selectResourcesByPage(Integer projectId, @Valid ProjectResourcePageDTO dto);
}
