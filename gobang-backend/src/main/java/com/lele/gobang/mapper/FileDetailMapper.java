package com.lele.gobang.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lele.gobang.model.domain.FileDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 勒勒
 * @description 针对表【file_detail(文件记录表)】的数据库操作Mapper
 * @createDate 2022-12-02 21:26:30
 */
@Mapper
public interface FileDetailMapper extends BaseMapper<FileDetail> {

}




