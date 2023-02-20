package com.lele.gobang.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.google.gson.JsonObject;
import com.lele.gobang.model.domain.OnePutLevel;
import com.lele.gobang.model.domain.OnePutLevelDetail;
import com.lele.gobang.model.domain.request.CheckAnsRequest;
import com.lele.gobang.model.dto.OnePutRankInfo;
import com.lele.gobang.model.dto.OnePutReputation;

import java.util.List;

/**
 * @author 勒勒
 * @description 针对表【one_put_level(关卡信息表)】的数据库操作Service
 * @createDate 2023-01-09 10:30:37
 */
public interface OnePutLevelService extends IService<OnePutLevel> {
    /**
     * 获得关卡答案
     */
    Boolean checkAns(CheckAnsRequest checkAnsRequest);

    /**
     * 获取oneput模式的前三名
     */
    List<OnePutRankInfo> getTopThree();

    /**
     * 获取oneput模式的排行榜
     */
    List<OnePutRankInfo> getRankList();

    /**
     * oneput个人信息
     */
    OnePutRankInfo getSelfInfo();

    /**
     * oneput 列表
     */
    @Deprecated
    List<OnePutLevelDetail> getOneputList();

    /**
     * oneput关卡分页展示
     */
    JsonObject page(Integer page);

    /**
     * 初始化oneput
     */
    void initOnePut(Integer id);

    /**
     * 贡献者列表
     */
    List<OnePutReputation> reputationList();

    /**
     * 查看用户贡献关卡列表
     *
     * @param userId 用户id
     */
    List<OnePutLevel> getSelfReputation(int userId);

    /**
     * 用户添加关卡
     */
    int add(OnePutLevel onePutLevel);

    /**
     * 用户修改关卡
     */
    int update(OnePutLevel onePutLevel);

    /**
     * 获取关卡数
     */
    Long getLevelCount();

    /**
     * 获取oneput模式关卡地图
     */
    String getOnePutMap(Integer level);

    /**
     * 初始化在线用户oneput
     */
    void initOnlineOnePut();
}
