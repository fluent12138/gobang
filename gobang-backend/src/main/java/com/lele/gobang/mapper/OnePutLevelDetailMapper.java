package com.lele.gobang.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lele.gobang.model.domain.OnePutLevelDetail;
import com.lele.gobang.model.dto.OnePutRankInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 勒勒
 * @description 针对表【one_put_level_detail(闯关详情表)】的数据库操作Mapper
 * @createDate 2023-01-09 10:35:20
 */
@Mapper
public interface OnePutLevelDetailMapper extends BaseMapper<OnePutLevelDetail> {
    /**
     * 获取排行榜
     *
     * @param count 前count名
     * @return 排行榜信息
     */
    List<OnePutRankInfo> getRankList(Integer count);

    /**
     * 获得自己的排名
     */
    OnePutRankInfo getSelfRank(Integer userid);
}




