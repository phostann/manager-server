package com.example.manager.controller.v1;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.manager.domain.dto.resource.ResourceCreateDTO;
import com.example.manager.domain.dto.resource.ResourceQueryDTO;
import com.example.manager.domain.dto.resource.ResourceUpdateDTO;
import com.example.manager.response.R;
import com.example.manager.service.IResourceService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 资源管理控制器
 */
@RestController
@RequestMapping("/v1/resource")
@Validated
public class ResourceController {

    @Resource
    private IResourceService resourceService;

    /**
     * 创建资源
     *
     * @param dto 资源创建DTO
     * @return 资源ID
     */
    @PostMapping("/create")
    public R<Void> create(@Validated @RequestBody ResourceCreateDTO dto) {
        resourceService.createResource(dto);
        return R.ok();
    }

    /**
     * 更新资源
     *
     * @param id  资源ID
     * @param dto 资源更新DTO
     * @return 响应结果
     */
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Integer id, @Validated @RequestBody ResourceUpdateDTO dto) {
        resourceService.updateResourceById(id, dto);
        return R.ok();
    }

    /**
     * 删除资源
     *
     * @param id 资源ID
     * @return 响应结果
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Integer id) {
        resourceService.deleteResource(id);
        return R.ok();
    }

    /**
     * 获取资源详情
     *
     * @param id 资源ID
     * @return 资源详情
     */
    @GetMapping("/{id}")
    public R<com.example.manager.entity.Resource> getById(@PathVariable Integer id) {
        com.example.manager.entity.Resource resource = resourceService.getResourceById(id);
        return R.ok(resource);
    }

    /**
     * 分页查询资源
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @GetMapping("/page")
    public R<Page<com.example.manager.entity.Resource>> page(@Valid ResourceQueryDTO queryDTO) {
        Page<com.example.manager.entity.Resource> page = resourceService.queryResources(queryDTO);
        return R.ok(page);
    }
} 