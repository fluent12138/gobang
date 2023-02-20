package com.lele.gobang.model.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @TableName life_user
 */
@TableName(value = "life_user")
@Data
@Builder
public class LifeUser implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 活多久
     */
    private Integer life;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 精确到几位
     */
    private Integer accurate;
    /**
     * 1 表示紧迫感 0 无紧迫感
     */
    private Integer tension;
    /**
     * 1 恋爱了 0 没有恋爱
     */
    private Integer loving;
    /**
     * 恋爱开始时间
     */
    private BigDecimal lovestart;
    /**
     * 生日
     */
    private BigDecimal birthday;
    /**
     * openid
     */
    private String openid;
}