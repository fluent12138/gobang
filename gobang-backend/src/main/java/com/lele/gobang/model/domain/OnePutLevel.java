package com.lele.gobang.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 关卡信息表
 *
 * @TableName one_put_level
 */
@TableName(value = "one_put_level")
@Data
public class OnePutLevel implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 关卡id
     */
    private Integer levelId;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 关卡描述
     */
    private String description;
    /**
     * 关卡地图
     */
    private String map;
    /**
     * 关卡状态: 0 - 已通过; 1 - 审核中; 2 - 未通过
     */
    private Integer status;
    /**
     * 管理员审核信息
     */
    private String statusInfo;
    /**
     * 答案横坐标
     */
    private Integer x;
    /**
     * 答案纵坐标
     */
    private Integer y;
}