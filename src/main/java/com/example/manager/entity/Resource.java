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
 * @since 2025-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("resource")
public class Resource extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 638183007901504150L;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 业务名称
     */
    private String appName;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 资源版本
     */
    private String version;
}
