package com.lele.gobang.controller.home;

import cn.dev33.satoken.stp.StpUtil;
import com.google.gson.JsonObject;
import com.lele.gobang.common.ErrorCode;
import com.lele.gobang.common.R;
import com.lele.gobang.common.ResultUtils;
import com.lele.gobang.model.domain.Home;
import com.lele.gobang.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lele
 * @create 2023-01-10 16:12
 */
@RestController
@RequestMapping("/home/")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @RequestMapping("create")
    public R creatHome(String password) {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        Home home = homeService.createHome(password);
        return ResultUtils.success(home);
    }

    @RequestMapping("remove")
    public R removeHome() {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        int res = homeService.removeHome();
        return ResultUtils.success(res);
    }

    @RequestMapping("join")
    public R joinHome(@RequestParam String homeId, String password) {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        int res = homeService.joinHome(homeId, password);
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
        JsonObject info = homeService.page(page);
        return ResultUtils.success(info.toString());
    }

    @RequestMapping("search")
    public R search(String homeId) {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        if (homeId == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR, "房间号不能为空");
        }
        Home home = homeService.searchByHomeId(homeId);
        return ResultUtils.success(home);
    }
}
