package com.lele.gobang.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lele.gobang.model.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 勒勒
 * @description 针对表【user(用户)】的数据库操作Mapper
 * @createDate 2023-01-06 09:57:07
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




