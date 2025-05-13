package com.example.manager.controller.v1;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.manager.domain.dto.project.*;
import com.example.manager.domain.vo.project.ProjectVO;
import com.example.manager.entity.Node;
import com.example.manager.entity.Project;
import com.example.manager.response.R;
import com.example.manager.service.IProjectService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/project")
@Validated
public class ProjectController {
    @Resource
    private IProjectService projectService;

    @PostMapping("/create")
    public R<Void> create(@Valid @RequestBody ProjectCreateDTO dto) {
        projectService.createProject(dto);
        return R.ok();
    }

    @PutMapping("/{projectId}")
    public R<Void> update(@PathVariable("projectId") Integer projectId, @Valid @RequestBody ProjectUpdateDTO dto) {
        projectService.updateProject(projectId, dto);
        return R.ok();
    }

    @GetMapping("/page")
    public R<Page<ProjectVO>> page(@Valid ProjectPageDTO dto) {
        Page<ProjectVO> page = projectService.selectProjectByPage(dto);
        return R.ok(page);
    }

    @GetMapping("/{projectId}")
    public R<Project> get(@PathVariable("projectId") Integer projectId) {
        Project project = projectService.selectProjectById(projectId);
        return R.ok(project);
    }

    @GetMapping("/{projectId}/resources/page")
    public R<Page<com.example.manager.entity.Resource>> getResources(@PathVariable Integer projectId, @Valid ProjectResourcePageDTO dto) {
        Page<com.example.manager.entity.Resource> resources = projectService.selectResourcesByPage(projectId, dto);
        return R.ok(resources);
    }

    @GetMapping("/{projectId}/nodes/page")
    public R<Page<Node>> getNodes(@PathVariable Integer projectId, @Valid ProjectNodePageDTO dto) {
        Page<Node> resources = projectService.selectNodesByPage(projectId, dto);
        return R.ok(resources);
    }

    @DeleteMapping("/{projectId}")
    public R<Void> delete(@PathVariable("projectId") Integer projectId) {
        projectService.deleteProject(projectId);
        return R.ok();
    }

    @PostMapping("/{projectId}/nodes/{nodeId}")
    public R<Void> bindNode(@PathVariable Integer projectId,
                            @PathVariable Integer nodeId) {
        projectService.bindNode(projectId, nodeId);
        return R.ok();
    }

    @DeleteMapping("/{projectId}/nodes/{nodeId}")
    public R<Void> unbindNode(@PathVariable Integer projectId,
                              @PathVariable Integer nodeId) {
        projectService.unbindNode(projectId, nodeId);
        return R.ok();
    }

    @PostMapping("/{projectId}/resources/batch")
    public R<Void> bindResources(@PathVariable Integer projectId,
                                 @RequestBody List<Integer> resourceIds) {
        projectService.bindResources(projectId, resourceIds);
        return R.ok();
    }

    @DeleteMapping("/{projectId}/resources/batch")
    public R<Void> unbindResources(@PathVariable Integer projectId,
                                   @RequestBody List<Integer> resourceIds) {
        projectService.unbindResources(projectId, resourceIds);
        return R.ok();
    }

    @GetMapping("/node/{nodeUid}/config")
    public R<String> getConfigByNodeId(@PathVariable Integer nodeUid) {
        String config = projectService.getConfigByNodeUid(nodeUid);
        return R.ok(config);
    }
}
