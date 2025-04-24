package com.example.manager.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.manager.domain.dto.common.PageDTO;
import com.example.manager.domain.vo.common.PageVO;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具类
 */
public class PageUtils {

    /**
     * 创建MyBatis-Plus分页对象
     *
     * @param pageDTO 分页参数
     * @param <T>     实体类型
     * @return MyBatis-Plus分页对象
     */
    public static <T> Page<T> getPage(PageDTO<T> pageDTO) {
        return pageDTO.toMybatisPage();
    }

    /**
     * 将MyBatis-Plus分页结果转换为自定义分页结果
     *
     * @param page MyBatis-Plus分页结果
     * @param <T>  数据类型
     * @return 自定义分页结果
     */
    public static <T> PageVO<T> toPageVO(IPage<T> page) {
        return PageVO.of(page.getRecords(), page.getTotal(), (int) page.getCurrent(), (int) page.getSize());
    }

    /**
     * 将MyBatis-Plus分页结果转换为自定义分页结果，并转换数据类型
     *
     * @param page     MyBatis-Plus分页结果
     * @param function 数据转换函数
     * @param <T>      源数据类型
     * @param <R>      目标数据类型
     * @return 自定义分页结果
     */
    public static <T, R> PageVO<R> toPageVO(IPage<T> page, Function<T, R> function) {
        List<R> list = page.getRecords().stream()
                .map(function)
                .collect(Collectors.toList());
        return PageVO.of(list, page.getTotal(), (int) page.getCurrent(), (int) page.getSize());
    }
} 