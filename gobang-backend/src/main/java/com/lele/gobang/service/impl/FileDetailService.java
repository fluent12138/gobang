package com.lele.gobang.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.recorder.FileRecorder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lele.gobang.mapper.FileDetailMapper;
import com.lele.gobang.model.domain.FileDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileDetailService extends ServiceImpl<FileDetailMapper, FileDetail> implements FileRecorder {
    /**
     * 保存文件信息到数据库
     */
    @Override
    public boolean record(FileInfo info) {
        FileDetail detail = BeanUtil.copyProperties(info, FileDetail.class);
        boolean b = save(detail);
        if (b) {
            info.setId(detail.getId());
        }
        return b;
    }

    /**
     * 根据 url 查询文件信息
     */
    @Override
    public FileInfo getByUrl(String url) {
        FileDetail detail = getOne(new QueryWrapper<FileDetail>().eq("url", url));
        FileInfo info = BeanUtil.copyProperties(detail, FileInfo.class, "attr");

        //这是手动获取数据库中的 json 字符串 并转成 附加属性字典，方便使用
        if (detail != null && StrUtil.isNotBlank(detail.getAttr())) {
            try {
                info.setAttr(new ObjectMapper().readValue(detail.getAttr(), Dict.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return info;
    }

    /**
     * 根据 url 删除文件信息
     */
    @Override
    public boolean delete(String url) {
        return remove(new QueryWrapper<FileDetail>().eq("url", url));
    }
}
