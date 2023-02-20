package com.lele.gobang.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏房间表
 *
 * @TableName home
 */
@TableName(value = "home")
@Data
@Builder
public class Home implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 房间id
     */
    private String homeId;
    /**
     * 房间拥有者id
     */
    private Integer aId;
    /**
     * 进入房间的人的id
     */
    private Integer bId;
    /**
     * 房间密码
     */
    private String password;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;
}