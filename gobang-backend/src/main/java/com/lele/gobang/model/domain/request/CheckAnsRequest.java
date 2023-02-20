package com.lele.gobang.model.domain.request;

import lombok.Data;

/**
 * @author lele
 * @create 2023-01-09 20:30
 */
@Data
public class CheckAnsRequest {
    /**
     * level id
     */
    private Integer id;
    /**
     * 下棋坐标
     */
    private Integer x;
    private Integer y;
    /**
     * 消耗时间
     */
    private Double passTime;
}
