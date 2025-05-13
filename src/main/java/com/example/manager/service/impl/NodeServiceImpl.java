package com.example.manager.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.manager.converter.NodeConverter;
import com.example.manager.domain.dto.node.NodeAutoSyncDTO;
import com.example.manager.domain.dto.node.NodeCreateDTO;
import com.example.manager.domain.dto.node.NodePageDTO;
import com.example.manager.domain.dto.node.NodeUpdateDTO;
import com.example.manager.domain.dto.project.ProjectNodePageDTO;
import com.example.manager.entity.Node;
import com.example.manager.exception.BusinessException;
import com.example.manager.mapper.NodeMapper;
import com.example.manager.response.ErrorCode;
import com.example.manager.service.INodeService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author phos
 * @since 2025-04-18
 */
@Service
public class NodeServiceImpl extends ServiceImpl<NodeMapper, Node> implements INodeService {

    @Override
    public void createNode(NodeCreateDTO dto) {
        // 检查同名节点是否存在
        boolean exists = this.lambdaQuery()
                .eq(Node::getName, dto.getName())
                .count() > 0;
        if (exists) {
            throw new BusinessException(ErrorCode.NODE_EXISTS);
        }
        // 创建新节点
        Node entity = NodeConverter.INSTANCE.toEntity(dto);
        boolean saved = this.save(entity);
        if (!saved) {
            // 如果保存失败，可能是因为数据库问题
            throw new BusinessException(ErrorCode.NODE_CREATE_FAILED);
        }
    }

    @Override
    public void updateNodeById(Integer id, NodeUpdateDTO dto) {
        // 检查节点是否存在
        Node node = this.lambdaQuery()
                .eq(Node::getId, id)
                .one();
        if (node == null) {
            throw new BusinessException(ErrorCode.NODE_NOT_FOUND);
        }
        // 检查同名节点是否存在
        if (StringUtils.hasText(dto.getName()) && !Objects.equals(node.getName(), dto.getName())) {
            boolean exists = this.lambdaQuery()
                    .eq(Node::getName, dto.getName())
                    .ne(Node::getId, id)
                    .count() > 0;
            if (exists) {
                throw new BusinessException(ErrorCode.NODE_EXISTS);
            }
        }
        // 更新节点信息
        boolean updated = this.lambdaUpdate()
                .set(Node::getName, dto.getName())
                .eq(Node::getId, id)
                .update();
        if (!updated) {
            throw new BusinessException(ErrorCode.NODE_UPDATE_FAILED);
        }
    }

    @Override
    public Node selectNodeById(Integer id) {
        // 检查节点是否存在
        Node node = this.lambdaQuery()
                .eq(Node::getId, id)
                .one();
        if (node == null) {
            throw new BusinessException(ErrorCode.NODE_NOT_FOUND);
        }
        return node;
    }

    @Override
    public void deleteNode(Integer id) {
        // 检查节点是否存在
        Node node = this.lambdaQuery()
                .eq(Node::getId, id)
                .one();
        if (node == null) {
            throw new BusinessException(ErrorCode.NODE_NOT_FOUND);
        }
        // 检查节点是否被项目绑定
        if (node.getProjectId() != null) {
            throw new BusinessException(ErrorCode.NODE_DELETE_FAILED, "节点已被项目绑定");
        }
        // 删除节点
        boolean removed = this.lambdaUpdate()
                .eq(Node::getId, id)
                .remove();
        if (!removed) {
            throw new BusinessException(ErrorCode.NODE_DELETE_FAILED);
        }
    }

    @Override
    public Page<Node> selectNodeByPage(NodePageDTO dto) {
        return this.lambdaQuery()
                .like(StringUtils.hasText(dto.getName()), Node::getName, dto.getName())
                .eq(dto.getStatus() != null, Node::getStatus, dto.getStatus())
                .page(dto.toMybatisPage());
    }

    @Override
    public List<Node> selectBindableNodeByPage(NodePageDTO dto) {
        return this.lambdaQuery()
                .isNull(Node::getProjectId)
                .list();
    }

    @Override
    public Page<Node> getNodesByProjectId(Integer projectId, ProjectNodePageDTO dto) {
        return this.lambdaQuery()
                .eq(Node::getProjectId, projectId)
                .like(StringUtils.hasText(dto.getName()), Node::getName, dto.getName())
                .page(dto.toMybatisPage());
    }

    @Override
    public void autoSync(Integer id, NodeAutoSyncDTO dto) {
        // 检查节点是否存在
        Node node = this.lambdaQuery()
                .eq(Node::getId, id)
                .one();
        if (node == null) {
            throw new BusinessException(ErrorCode.NODE_NOT_FOUND);
        }
        // 更新节点自动同步状态
        boolean updated = this.lambdaUpdate()
                .set(Node::getAutoSync, dto.getAutoSync())
                .eq(Node::getId, id)
                .update();
        if (!updated) {
            throw new BusinessException(ErrorCode.NODE_UPDATE_FAILED);
        }
    }

    @Override
    public Node getNodeByUid(Integer nodeUid) {
        return this.lambdaQuery()
                .eq(Node::getUid, nodeUid)
                .one();
    }
}
