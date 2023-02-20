package com.lele.gobang.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 闯关详情表
 *
 * @TableName one_put_level_detail
 */
@TableName(value = "one_put_level_detail")
@Data
@Builder
public class OnePutLevelDetail implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 关卡id
     */
    private Integer levelId;
    /**
     * 是否通关 0 - 未通过 1 - 已通过
     */
    private Integer isPass;
    /**
     * 是否游玩 0 - 未游玩 1 - 已游玩
     */
    private Integer isPlay;
    /**
     * 通关时间 - 单位s
     */
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = "%.2f")
    private Double passTime;
}