package com.lele.gobang.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件记录表
 *
 * @TableName file_detail
 */
@TableName("file_detail")
@Getter
@Setter
public class FileDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 文件id
     */
    private String id;
    /**
     * 文件访问地址
     */
    private String url;
    /**
     * 文件大小，单位字节
     */
    private Long size;
    /**
     * 文件名称
     */
    private String filename;
    /**
     * 原始文件名
     */
    private String originalFilename;
    /**
     * 基础存储路径
     */
    private String basePath;
    /**
     * 存储路径
     */
    private String path;
    /**
     * 文件扩展名
     */
    private String ext;
    /**
     * MIME类型
     */
    private String contentType;
    /**
     * 存储平台
     */
    private String platform;
    /**
     * 缩略图访问路径
     */
    private String thUrl;
    /**
     * 缩略图名称
     */
    private String thFilename;
    /**
     * 缩略图大小，单位字节
     */
    private Long thSize;
    /**
     * 缩略图MIME类型
     */
    private String thContentType;
    /**
     * 文件所属对象id
     */
    private String objectId;
    /**
     * 文件所属对象类型，例如用户头像，评价图片
     */
    private String objectType;
    /**
     * 附加属性
     */
    private String attr;
    /**
     * 创建时间
     */
    private Date createTime;
}