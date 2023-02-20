package com.lele.gobang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lele.gobang.model.domain.OnePutLevel;
import com.lele.gobang.model.dto.OnePutReputation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @author 勒勒
 * @description 针对表【one_put_level(关卡信息表)】的数据库操作Mapper
 * @createDate 2023-01-09 10:30:36
 */
@Mapper
public interface OnePutLevelMapper extends BaseMapper<OnePutLevel> {
    List<OnePutReputation> getReputationList();

    Set<Integer> getPassLevelIds();
}




