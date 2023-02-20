package com.lele.gobang.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lele.gobang.model.domain.Home;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 勒勒
 * @description 针对表【home(游戏房间表)】的数据库操作Mapper
 * @createDate 2023-01-10 15:39:55
 */
@Mapper
public interface HomeMapper extends BaseMapper<Home> {

}




