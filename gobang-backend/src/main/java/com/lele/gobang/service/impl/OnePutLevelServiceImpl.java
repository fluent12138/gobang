package com.lele.gobang.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import com.lele.gobang.common.ErrorCode;
import com.lele.gobang.exception.BusinessException;
import com.lele.gobang.mapper.OnePutLevelDetailMapper;
import com.lele.gobang.mapper.OnePutLevelMapper;
import com.lele.gobang.mapper.UserMapper;
import com.lele.gobang.model.domain.OnePutLevel;
import com.lele.gobang.model.domain.OnePutLevelDetail;
import com.lele.gobang.model.domain.User;
import com.lele.gobang.model.domain.request.CheckAnsRequest;
import com.lele.gobang.model.dto.OnePutRankInfo;
import com.lele.gobang.model.dto.OnePutReputation;
import com.lele.gobang.service.OnePutLevelService;
import com.lele.gobang.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 勒勒
 * @description 针对表【one_put_level(关卡信息表)】的数据库操作Service实现
 * @createDate 2023-01-09 10:30:37
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OnePutLevelServiceImpl extends ServiceImpl<OnePutLevelMapper, OnePutLevel>
        implements OnePutLevelService {
    private final UserMapper userMapper;
    private final OnePutLevelDetailMapper onePutLevelDetailMapper;

    @Override
    public Boolean checkAns(CheckAnsRequest checkAnsRequest) {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (checkAnsRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<OnePutLevel> queryWrapperForLevel = new QueryWrapper<>();
        queryWrapperForLevel.eq("level_id", checkAnsRequest.getId());
        OnePutLevel onePutLevel = this.baseMapper.selectOne(queryWrapperForLevel);
        var res = onePutLevel.getX().equals(checkAnsRequest.getX()) && onePutLevel.getY().equals(checkAnsRequest.getY());
        if (res) {
            int userid = StpUtil.getLoginIdAsInt();
            QueryWrapper<OnePutLevelDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userid).eq("level_id", checkAnsRequest.getId());
            if (checkAnsRequest.getPassTime() > 10) {
                checkAnsRequest.setPassTime(10.0);
            }
            OnePutLevelDetail detail = OnePutLevelDetail.builder()
                    .levelId(checkAnsRequest.getId())
                    .isPlay(1)
                    .isPass(1)
                    .userId(userid)
                    .passTime(checkAnsRequest.getPassTime())
                    .build();
            int update = onePutLevelDetailMapper.update(detail, queryWrapper);
            if (update <= 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
        return res;
    }

    @Override
    public List<OnePutRankInfo> getTopThree() {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        List<OnePutRankInfo> topThree = onePutLevelDetailMapper.getRankList(3);
        return topThree;
    }

    @Override
    public List<OnePutRankInfo> getRankList() {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        List<OnePutRankInfo> users = new ArrayList<>();
        int userid = StpUtil.getLoginIdAsInt();
        OnePutRankInfo selfRank = onePutLevelDetailMapper.getSelfRank(userid);
        users.add(selfRank);
        onePutLevelDetailMapper.getRankList(10)
                .stream()
                .peek(users::add).collect(Collectors.toList());
        return users;
    }

    @Override
    public OnePutRankInfo getSelfInfo() {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int userid = StpUtil.getLoginIdAsInt();
        OnePutRankInfo selfRank = onePutLevelDetailMapper.getSelfRank(userid);
        return selfRank;
    }

    @Override
    @Deprecated
    public List<OnePutLevelDetail> getOneputList() {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int userid = StpUtil.getLoginIdAsInt();
        QueryWrapper<OnePutLevelDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userid);
        List<OnePutLevelDetail> oneputList = onePutLevelDetailMapper.selectList(queryWrapper);
        return oneputList;
    }

    @Override
    public void initOnePut(Integer id) {
        QueryWrapper<OnePutLevelDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        List<OnePutLevelDetail> onePutLevelDetailList = onePutLevelDetailMapper.selectList(queryWrapper);
        Long levelCount = getLevelCount();
        if (onePutLevelDetailList.size() == levelCount) {
            log.info("userId : {}, 该用户oneput数据已经同步完成", id);
            return;
        }
        OnePutLevelDetail onePutLevelDetail = OnePutLevelDetail.builder()
                .userId(id)
                .isPass(0)
                .isPlay(0)
                .passTime(0.0)
                .build();
        for (int i = 1; i <= levelCount; i++) {
            queryWrapper.clear();
            queryWrapper.eq("user_id", id);
            queryWrapper.eq("level_id", i);
            OnePutLevelDetail detail = onePutLevelDetailMapper.selectOne(queryWrapper);
            if (detail != null) {
                continue;
            }
            onePutLevelDetail.setLevelId(i);
            int insert = onePutLevelDetailMapper.insert(onePutLevelDetail);
            if (insert <= 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
            onePutLevelDetail.setId(onePutLevelDetail.getId() + 1);
        }
    }

    @Override
    public List<OnePutReputation> reputationList() {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        List<OnePutReputation> reputationList = this.baseMapper.getReputationList();
        return reputationList;
    }

    @Override
    public List<OnePutLevel> getSelfReputation(int userId) {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        QueryWrapper<OnePutLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderByDesc("status");
        List<OnePutLevel> onePutLevelList = this.baseMapper.selectList(queryWrapper);
        return onePutLevelList;
    }

    @Override
    public int add(OnePutLevel onePutLevel) {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (onePutLevel == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 答案坐标校验
        if (onePutLevel.getX() == null || onePutLevel.getY() == null
                || onePutLevel.getX() < 0 || onePutLevel.getX() > 11
                || onePutLevel.getY() < 0 || onePutLevel.getY() > 11) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // map, 描述不能为空
        if (StringUtils.isAnyBlank(onePutLevel.getMap(), onePutLevel.getDescription())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 设置为未审核
        onePutLevel.setStatus(1);
        int insert = this.baseMapper.insert(onePutLevel);
        if (insert < 1) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return insert;
    }

    @Override
    public int update(OnePutLevel onePutLevel) {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (onePutLevel == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 答案坐标校验
        if (onePutLevel.getX() == null || onePutLevel.getY() == null
                || onePutLevel.getX() < 0 || onePutLevel.getX() > 11
                || onePutLevel.getY() < 0 || onePutLevel.getY() > 11) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // map, 描述不能为空
        if (StringUtils.isAnyBlank(onePutLevel.getMap(), onePutLevel.getDescription())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 设置为未审核
        onePutLevel.setStatus(1);
        // 覆盖错误信息
        onePutLevel.setStatusInfo("");
        System.out.println(onePutLevel);
        int update = this.baseMapper.updateById(onePutLevel);
        if (update < 1) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return update;
    }

    @Override
    public Long getLevelCount() {
        QueryWrapper<OnePutLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0);
        Long count = this.baseMapper.selectCount(queryWrapper);
        return count;
    }

    @Override
    public String getOnePutMap(Integer levelId) {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        QueryWrapper<OnePutLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level_id", levelId);
        OnePutLevel onePutLevel = this.baseMapper.selectOne(queryWrapper);
        if (onePutLevel == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String map = onePutLevel.getMap();
        if (StringUtils.isBlank(map)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return map;
    }

    @Override
    public void initOnlineOnePut() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("id", 0);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.stream().filter(user -> {
            String token = StpUtil.getTokenValueByLoginId(user.getId());
            Object id = StpUtil.getLoginIdByToken(token);
            return !Objects.isNull(id);
        }).forEach(user -> initOnePut(user.getId()));
    }

    @Override
    public JsonObject page(Integer page) {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int userid = StpUtil.getLoginIdAsInt();
        IPage<OnePutLevelDetail> onePutLevelDetailPage = new Page<>(page, 8);
        QueryWrapper<OnePutLevelDetail> query = new QueryWrapper<>();
        query.eq("user_id", userid);
        List<OnePutLevelDetail> levelList = onePutLevelDetailMapper.selectPage(onePutLevelDetailPage, query).getRecords();
        Long count = getLevelCount();

        Set<Integer> passLevelIds = this.baseMapper.getPassLevelIds();
        levelList = levelList.stream().filter(levelDetail -> passLevelIds.contains(levelDetail.getLevelId())).collect(Collectors.toList());
        JsonObject resp = new JsonObject();
        resp.add("levels", JsonUtils.getJsonElement(levelList));
        resp.addProperty("count", count);
        return resp;
    }

}




