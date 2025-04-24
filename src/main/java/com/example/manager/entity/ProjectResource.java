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
@TableName("project_resource")
public class ProjectResource extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -6686782501130400065L;

    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 资源ID
     */
    private Integer resourceId;

    /**
     * 资源版本
     */
    private String version;
}
