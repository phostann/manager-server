package com.example.manager.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.manager.converter.ResourceConverter;
import com.example.manager.domain.dto.project.ProjectResourcePageDTO;
import com.example.manager.domain.dto.resource.ResourceCreateDTO;
import com.example.manager.domain.dto.resource.ResourceQueryDTO;
import com.example.manager.domain.dto.resource.ResourceUpdateDTO;
import com.example.manager.entity.Resource;
import com.example.manager.enums.VersionUpdateType;
import com.example.manager.exception.BusinessException;
import com.example.manager.mapper.ResourceMapper;
import com.example.manager.response.ErrorCode;
import com.example.manager.service.IResourceService;
import com.example.manager.utils.VersionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 资源服务实现类
 * </p>
 *
 * @author phos
 * @since 2025-04-17
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Override
    public void createResource(ResourceCreateDTO dto) {
        // 检查同名资源是否存在
        boolean exists = this.lambdaQuery()
                .eq(Resource::getName, dto.getName())
                .eq(Resource::getAppName, dto.getAppName())
                .count() > 0;

        if (exists) {
            throw new BusinessException(ErrorCode.RESOURCE_EXISTS);
        }

        Resource resource = ResourceConverter.INSTANCE.toEntity(dto);
        // 初始版本号 0.0.1
        resource.setVersion("0.0.1");
        boolean saved = this.save(resource);
        if (!saved) {
            throw new BusinessException(ErrorCode.RESOURCE_CREATE_FAILED);
        }
    }

    @Override
    public void updateResourceById(Integer id, ResourceUpdateDTO dto) {
        // 检查资源是否存在
        Resource resource = this.lambdaQuery()
                .eq(Resource::getId, id)
                .one();

        if (resource == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        // 如果更新名称，检查名称是否冲突
        if (StringUtils.hasText(dto.getName()) || StringUtils.hasText(dto.getAppName())) {
            boolean exists = this.lambdaQuery()
                    .eq(StringUtils.hasText(dto.getName()), Resource::getName, dto.getName())
                    .or()
                    .eq(StringUtils.hasText(dto.getAppName()), Resource::getAppName, dto.getAppName())
                    .ne(Resource::getId, id)
                    .count() > 0;

            if (exists) {
                throw new BusinessException(ErrorCode.RESOURCE_EXISTS);
            }
        }

        // 更新资源
        boolean updated = lambdaUpdate()
                .eq(Resource::getId, id)
                .set(StringUtils.hasText(dto.getName()), Resource::getName, dto.getName())
                .set(StringUtils.hasText(dto.getAppName()), Resource::getAppName, dto.getAppName())
                .set(StringUtils.hasText(dto.getDescription()), Resource::getDescription, dto.getDescription())
                .set(Resource::getVersion, VersionUtils.upgrade(resource.getVersion(), VersionUpdateType.fromCode(dto.getUpdateType())))
                .update();

        if (!updated) {
            throw new BusinessException(ErrorCode.RESOURCE_UPDATE_FAILED);
        }
    }

    @Override
    public void deleteResource(Integer id) {
        // 删除资源（逻辑删除）
        boolean removed = lambdaUpdate()
                .eq(Resource::getId, id)
                .remove();

        if (!removed) {
            throw new BusinessException(ErrorCode.RESOURCE_DELETE_FAILED);
        }
    }

    @Override
    public Resource getResourceById(Integer id) {
        // 获取资源
        Resource resource = lambdaQuery()
                .eq(Resource::getId, id)
                .one();

        if (resource == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        return resource;
    }

    @Override
    public Page<Resource> queryResources(ResourceQueryDTO dto) {
        // 分页查询
        return lambdaQuery()
                .eq(StringUtils.hasText(dto.getName()), Resource::getName, dto.getName())
                .like(StringUtils.hasText(dto.getAppName()), Resource::getAppName, dto.getAppName())
                .eq(StringUtils.hasText(dto.getVersion()), Resource::getVersion, dto.getVersion())
                .orderByDesc(Resource::getCreatedAt)
                .page(dto.toMybatisPage());
    }

    @Override
    public List<Resource> getResourceByIds(List<Integer> resourceIds) {
        return this.lambdaQuery()
                .in(Resource::getId, resourceIds)
                .list();
    }

    @Override
    public Page<Resource> getResourcesByProjectId(Integer projectId, ProjectResourcePageDTO dto) {
        return this.baseMapper
                .selectResourcesByProjectId(dto.toMybatisPage(), projectId, dto);
    }
}
