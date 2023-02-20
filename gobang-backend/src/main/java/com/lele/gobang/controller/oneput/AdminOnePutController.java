package com.lele.gobang.controller.oneput;

import cn.dev33.satoken.stp.StpUtil;
import com.lele.gobang.common.ErrorCode;
import com.lele.gobang.common.R;
import com.lele.gobang.common.ResultUtils;
import com.lele.gobang.model.domain.OnePutLevel;
import com.lele.gobang.model.domain.User;
import com.lele.gobang.service.OnePutLevelService;
import com.lele.gobang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lele
 * @create 2023-02-08 11:58
 */
@RestController
@RequestMapping("/admin/oneput/")
@RequiredArgsConstructor
public class AdminOnePutController {
    private final OnePutLevelService onePutService;
    private final UserService userService;

    @RequestMapping("update")
    public R update(@RequestBody OnePutLevel onePutLevel) {
        ErrorCode errorCode = checkError();
        if (errorCode.getCode() > 0) {
            return ResultUtils.error(errorCode);
        }
        if (onePutLevel == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (onePutLevel.getStatus() == 0) {
            Long count = onePutService.getLevelCount();
            onePutLevel.setLevelId(count.intValue() + 1);
        }
        boolean update = onePutService.updateById(onePutLevel);
        if (!update) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        onePutService.initOnlineOnePut();
        return ResultUtils.success();
    }

    @RequestMapping("list")
    public R list() {
        ErrorCode errorCode = checkError();
        if (errorCode.getCode() > 0) {
            return ResultUtils.error(errorCode);
        }
        List<OnePutLevel> list = onePutService.list();
        list = list.stream().sorted(
                Comparator.comparing(OnePutLevel::getStatus, (x, y) -> {
                    if (x <= y) {
                        return 1;
                    } else {
                        return -1;
                    }
                })
        ).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @RequestMapping("delete")
    public R delete(@RequestBody OnePutLevel onePutLevel) {
        ErrorCode errorCode = checkError();
        if (errorCode.getCode() > 0) {
            return ResultUtils.error(errorCode);
        }
        boolean remove = onePutService.removeById(onePutLevel);
        return ResultUtils.success(remove);
    }

    private ErrorCode checkError() {
        if (!StpUtil.isLogin()) {
            return ErrorCode.NOT_LOGIN;
        }
        int userId = StpUtil.getLoginIdAsInt();
        User user = userService.getById(userId);
        // 用户为空或者无权限则返回
        if (user == null || !user.getRole().equals(1)) {
            return ErrorCode.NO_AUTH;
        }
        return ErrorCode.SUCCESS;
    }
}
