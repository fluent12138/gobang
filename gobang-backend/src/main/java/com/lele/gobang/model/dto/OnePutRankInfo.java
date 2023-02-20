package com.lele.gobang.model.dto;

import lombok.Data;

/**
 * oneput 排行榜信息DTO
 *
 * @author lele
 * @create 2023-01-09 11:22
 */
@Data
public class OnePutRankInfo {
    private String username;
    private String avatar;
    private Integer passCount;
    private Double passTime;
    private Integer rank;
}
