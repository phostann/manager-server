package com.example.manager.domain.dto.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * 分页参数DTO
 */
@Data
public class PageDTO<T> {
    /**
     * 页码，默认值为1
     */
    @Positive(message = "页码必须为正整数")
    private Integer page = 1;
    
    /**
     * 每页记录数，默认值为10
     */
    @Positive(message = "每页大小必须为正整数")
    private Integer size = 10;

    /**
     * 获取MySQL分页的偏移量
     *
     * @return 偏移量
     */
    public Integer getOffset() {
        return (page - 1) * size;
    }
    
    /**
     * 转换为MyBatis Plus分页对象
     */
    public Page<T> toMybatisPage() {
        return new Page<>(page, size);
    }
} 