package com.example.manager.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author phos
 * @since 2025-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("node")
public class Node extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -3778162153851080809L;

    /**
     * uid
     */
    private String uid;

    /**
     * 节点名称
     */
    private String name;

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * 节点状态; 0:离线 1:在线
     */
    private Integer status;

    /**
     * 项目 ID
     */
    private Integer projectId;

    /**
     * 是否自动同步
     */
    private Boolean autoSync;

    /**
     * 同步状态; 0: 未同步; 1: 同步中; 2: 同步失败; 3: 同步成功;
     */
    private Integer syncStatus;

    /**
     * 是否已同步
     */
    private Boolean deployed;
}
