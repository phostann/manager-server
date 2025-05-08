package com.example.manager.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.manager.domain.dto.project.*;
import com.example.manager.domain.vo.project.ProjectVO;
import com.example.manager.entity.Node;
import com.example.manager.entity.Project;
import com.example.manager.exception.BusinessException;
import com.example.manager.mapper.ProjectMapper;
import com.example.manager.response.ErrorCode;
import com.example.manager.service.INodeService;
import com.example.manager.service.IProjectResourceService;
import com.example.manager.service.IProjectService;
import com.example.manager.service.IResourceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author phos
 * @since 2025-04-17
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Resource
    private INodeService nodeService;

    @Resource
    private IProjectResourceService projectResourceService;

    @Resource
    private IResourceService resourceService;

    @Override
    public void createProject(ProjectCreateDTO dto) {
        // 检查项目名称是否已存在
        boolean projectExists = this.lambdaQuery()
                .eq(Project::getName, dto.getName())
                .exists();
        if (projectExists) {
            throw new BusinessException(ErrorCode.PROJECT_EXISTS);
        }
        Project project = new Project();
        project.setName(dto.getName());
        boolean saved = this.save(project);
        if (!saved) {
            // 如果保存失败，可能是因为数据库问题
            throw new BusinessException(ErrorCode.PROJECT_CREATE_FAILED);
        }
    }

    @Override
    public void updateProject(Integer id, ProjectUpdateDTO dto) {
        // 检查项目是否存在
        Project project = this.getById(id);
        if (project == null) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);
        }
        if (StringUtils.hasText(dto.getName()) && !Objects.equals(project.getName(), dto.getName())) {
            // 检查项目名称是否已存在
            boolean exists = this.lambdaQuery()
                    .eq(Project::getName, dto.getName())
                    .ne(Project::getId, id)
                    .exists();
            if (exists) {
                throw new BusinessException(ErrorCode.PROJECT_EXISTS);
            }
        }

        // 更新项目
        boolean updated = this.lambdaUpdate()
                .eq(Project::getId, id)
                .set(StringUtils.hasText(dto.getName()), Project::getName, dto.getName())
                .set(StringUtils.hasText(dto.getConfig()), Project::getConfig, dto.getConfig())
                .update();
        if (!updated) {
            throw new BusinessException(ErrorCode.PROJECT_UPDATE_FAILED);
        }
    }

    @Override
    public Page<ProjectVO> selectProjectByPage(ProjectPageDTO dto) {
        return this.baseMapper.selectProjects(dto.toMybatisPage(), dto);
    }

    @Override
    public Project selectProjectById(Integer id) {
        Project project = this.lambdaQuery()
                .eq(Project::getId, id)
                .one();
        if (project == null) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);
        }
        return project;
    }

    @Override
    public void deleteProject(Integer id) {
        // 检查项目是否存在
        boolean removed = this.lambdaUpdate().eq(Project::getId, id).remove();
        if (!removed) {
            // 如果删除失败，可能是因为项目不存在
            throw new BusinessException(ErrorCode.PROJECT_DELETE_FAILED);
        }
    }

    @Override
    public void bindNode(Integer id, Integer nodeId) {
        // 检查项目是否存在
        Project project = this.getById(id);
        if (project == null) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);
        }
        // 查询节点是否已经被其他项目绑定
        Node node = nodeService.lambdaQuery()
                .eq(Node::getId, nodeId)
                .isNotNull(Node::getProjectId)
                .ne(Node::getProjectId, project.getId())
                .one();
        if (node != null) {
            throw new BusinessException(ErrorCode.PROJECT_NODE_BIND_FAILED, "节点已被其他项目绑定");
        }
        // 绑定节点，实际上是将节点的 projectID 设置为当前项目的 ID
        boolean updated = nodeService.lambdaUpdate()
                .eq(Node::getId, nodeId)
                .set(Node::getProjectId, id)
                .set(Node::getAutoSync, false) // 绑定后设置为未同步
                .update();
        if (!updated) {
            // 如果绑定失败，可能是因为节点不存在
            throw new BusinessException(ErrorCode.PROJECT_NODE_BIND_FAILED);
        }
    }

    @Override
    public void unbindNode(Integer projectId, Integer nodeId) {
        // 检查项目是否存在
        Project project = this.getById(projectId);
        if (project == null) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);
        }
        // 检查节点是否
        Node node = nodeService.lambdaQuery()
                .eq(Node::getProjectId, projectId)
                .eq(Node::getId, nodeId)
                .one();
        if (node == null) {
            throw new BusinessException(ErrorCode.PROJECT_NODE_UNBIND_FAILED, "未绑定任何节点");
        }
        // 解绑节点，实际上是将节点的 projectID 设置为 null
        boolean updated = nodeService.lambdaUpdate()
                .eq(Node::getId, nodeId)
                .set(Node::getProjectId, null)
                .set(Node::getAutoSync, false)
                .update();
        if (!updated) {
            // 如果解绑失败，可能是因为节点不存在
            throw new BusinessException(ErrorCode.PROJECT_NODE_UNBIND_FAILED);
        }
    }

    @Override
    public void bindResources(Integer projectId, List<Integer> resourceIds) {
        // 检查项目是否存在
        Project project = this.getById(projectId);
        if (project == null) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);
        }
        // 绑定资源
        boolean updated = projectResourceService.bindResources(projectId, resourceIds);
        if (!updated) {
            // 如果绑定失败，可能是因为资源不存在
            throw new BusinessException(ErrorCode.PROJECT_RESOURCE_BIND_FAILED);
        }

    }

    @Override
    public void unbindResources(Integer projectId, List<Integer> resourceIds) {
        // 检查项目是否存在
        Project project = this.getById(projectId);
        if (project == null) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);
        }
        // 解绑资源
        boolean updated = projectResourceService.unbindResources(projectId, resourceIds);
        if (!updated) {
            // 如果解绑失败，可能是因为资源不存在
            throw new BusinessException(ErrorCode.PROJECT_RESOURCE_UNBIND_FAILED);
        }
    }

    @Override
    public Page<com.example.manager.entity.Resource> selectResourcesByPage(Integer projectId, ProjectResourcePageDTO dto) {
        return resourceService.getResourcesByProjectId(projectId, dto);
    }

    @Override
    public Page<Node> selectNodesByPage(Integer projectId, ProjectNodePageDTO dto) {
        return nodeService.getNodesByProjectId(projectId, dto);
    }
}
