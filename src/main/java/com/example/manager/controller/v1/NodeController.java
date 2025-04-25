package com.example.manager.controller.v1;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.manager.domain.dto.node.NodeAutoSyncDTO;
import com.example.manager.domain.dto.node.NodeCreateDTO;
import com.example.manager.domain.dto.node.NodePageDTO;
import com.example.manager.domain.dto.node.NodeUpdateDTO;
import com.example.manager.entity.Node;
import com.example.manager.response.R;
import com.example.manager.service.INodeService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/node")
public class NodeController {

    @Resource
    private INodeService nodeService;

    @PostMapping("/create")
    public R<Void> create(@Valid @RequestBody NodeCreateDTO dto) {
        nodeService.createNode(dto);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Integer id, @Valid @RequestBody NodeUpdateDTO dto) {
        nodeService.updateNodeById(id, dto);
        return R.ok();
    }

    @GetMapping("/{id}")
    public R<Node> get(@PathVariable Integer id) {
        Node node = nodeService.selectNodeById(id);
        return R.ok(node);
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Integer id) {
        nodeService.deleteNode(id);
        return R.ok();
    }

    @GetMapping("/page")
    public R<Page<Node>> list(@Valid NodePageDTO dto) {
        Page<Node> page = nodeService.selectNodeByPage(dto);
        return R.ok(page);
    }

    @GetMapping("/bindable/all")
    public R<List<Node>> bindableList(@Valid NodePageDTO dto) {
        List<Node> page = nodeService.selectBindableNodeByPage(dto);
        return R.ok(page);
    }

    // 开关自动同步
    @PutMapping("/{id}/auto-sync")
    public R<Void> autoSync(@PathVariable Integer id, @Valid @RequestBody NodeAutoSyncDTO dto) {
        nodeService.autoSync(id, dto);
        return R.ok();
    }
}
