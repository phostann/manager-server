package com.example.manager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.manager.domain.dto.node.NodeCreateDTO;
import com.example.manager.domain.dto.node.NodePageDTO;
import com.example.manager.domain.dto.node.NodeUpdateDTO;
import com.example.manager.entity.Node;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author phos
 * @since 2025-04-18
 */
public interface INodeService extends IService<Node> {

    void createNode(@Valid NodeCreateDTO dto);

    void updateNodeById(Integer id, @Valid NodeUpdateDTO dto);

    Node selectNodeById(Integer id);

    void deleteNode(Integer id);

    Page<Node> selectNodeByPage(NodePageDTO dto);

    List<Node> selectBindableNodeByPage(@Valid NodePageDTO dto);
}
