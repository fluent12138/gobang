package com.lele.gobang.controller.oneput;

import cn.dev33.satoken.stp.StpUtil;
import com.google.gson.JsonObject;
import com.lele.gobang.common.ErrorCode;
import com.lele.gobang.common.R;
import com.lele.gobang.common.ResultUtils;
import com.lele.gobang.model.domain.OnePutLevel;
import com.lele.gobang.model.domain.request.CheckAnsRequest;
import com.lele.gobang.model.dto.OnePutRankInfo;
import com.lele.gobang.model.dto.OnePutReputation;
import com.lele.gobang.service.OnePutLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lele
 * @create 2023-01-09 10:41
 */
@RestController
@RequestMapping("/oneput/")
@RequiredArgsConstructor
public class OnePutController {
    private final OnePutLevelService onePutService;

    @RequestMapping("map")
    public R getMap(Integer levelId) {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        String map = onePutService.getOnePutMap(levelId);
        return ResultUtils.success(map);
    }

    @RequestMapping("check")
    public R checkAns(@RequestBody CheckAnsRequest checkAnsRequest) {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        if (checkAnsRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Boolean res = onePutService.checkAns(checkAnsRequest);
        return ResultUtils.success(res);
    }

    @RequestMapping("page")
    public R page(@RequestParam Integer page) {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        if (page == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR, "页数不能为空");
        }
        JsonObject info = onePutService.page(page);
        return ResultUtils.success(info.toString());
    }

    @RequestMapping("topThree")
    public R getTopThree() {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        List<OnePutRankInfo> topThree = onePutService.getTopThree();
        return ResultUtils.success(topThree);
    }

    @RequestMapping("rankList")
    public R getRankList() {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        List<OnePutRankInfo> rankList = onePutService.getRankList();
        return ResultUtils.success(rankList);
    }

    @RequestMapping("selfInfo")
    public R getSelfInfo() {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        OnePutRankInfo info = onePutService.getSelfInfo();
        return ResultUtils.success(info);
    }

    @RequestMapping("reputationList")
    public R reputationList() {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        List<OnePutReputation> info = onePutService.reputationList();
        return ResultUtils.success(info);
    }

    @RequestMapping("reputation")
    public R reputation() {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        int userId = StpUtil.getLoginIdAsInt();
        List<OnePutLevel> selfReputation = onePutService.getSelfReputation(userId);
        return ResultUtils.success(selfReputation);
    }

    @RequestMapping("add")
    public R add(@RequestBody OnePutLevel onePutLevel) {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        if (onePutLevel == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        int add = onePutService.add(onePutLevel);
        return ResultUtils.success(add);
    }

    @RequestMapping("update")
    public R update(@RequestBody OnePutLevel onePutLevel) {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        if (onePutLevel == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        int update = onePutService.update(onePutLevel);
        return ResultUtils.success(update);
    }

    @RequestMapping("delete")
    public R delete(@RequestBody OnePutLevel onePutLevel) {
        System.out.println(onePutLevel);
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        if (onePutLevel == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (onePutLevel.getStatus() == 0) {
            return ResultUtils.error(ErrorCode.NO_AUTH);
        }
        boolean remove = onePutService.removeById(onePutLevel);
        return ResultUtils.success(remove);
    }
}
