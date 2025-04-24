package com.example.manager.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.time.LocalDateTime;
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
     * 是否同步过当前项目资源；0 未同步；1 已同步；
     */
    private Boolean synced;
}
