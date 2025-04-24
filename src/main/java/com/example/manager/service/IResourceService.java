package com.example.manager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.manager.domain.dto.project.ProjectResourcePageDTO;
import com.example.manager.domain.dto.resource.ResourceCreateDTO;
import com.example.manager.domain.dto.resource.ResourceQueryDTO;
import com.example.manager.domain.dto.resource.ResourceUpdateDTO;
import com.example.manager.entity.Resource;

import java.util.List;

/**
 * <p>
 * 资源服务接口
 * </p>
 *
 * @author phos
 * @since 2025-04-17
 */
public interface IResourceService extends IService<Resource> {

    /**
     * 创建资源
     *
     * @param dto 资源创建DTO
     */
    void createResource(ResourceCreateDTO dto);

    /**
     * 更新资源
     *
     * @param dto 资源更新DTO
     */
    void updateResourceById(Integer id, ResourceUpdateDTO dto);

    /**
     * 删除资源
     *
     * @param id 资源ID
     */
    void deleteResource(Integer id);

    /**
     * 获取资源详情
     *
     * @param id 资源ID
     * @return 资源VO
     */
    Resource getResourceById(Integer id);

    /**
     * 分页查询资源
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<Resource> queryResources(ResourceQueryDTO queryDTO);

    List<Resource> getResourceByIds(List<Integer> resourceIds);


    Page<Resource> getResourcesByProjectId(Integer projectId, ProjectResourcePageDTO dto);
}
