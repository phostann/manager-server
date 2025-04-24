package com.example.manager.domain.vo.common;

import lombok.Data;
import java.util.List;

/**
 * 分页结果VO
 * @param <T> 数据类型
 */
@Data
public class PageVO<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页记录数
     */
    private Integer size;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 创建分页结果
     *
     * @param list  数据列表
     * @param total 总记录数
     * @param page  当前页码
     * @param size  每页记录数
     * @return 分页结果
     */
    public static <T> PageVO<T> of(List<T> list, Long total, Integer page, Integer size) {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setList(list);
        pageVO.setTotal(total);
        pageVO.setPage(page);
        pageVO.setSize(size);
        
        // 计算总页数
        long totalPages = total % size == 0 ? total / size : total / size + 1;
        pageVO.setTotalPages((int) totalPages);
        
        return pageVO;
    }
} 