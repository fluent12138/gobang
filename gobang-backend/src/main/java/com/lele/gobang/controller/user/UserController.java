package com.lele.gobang.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import com.google.gson.JsonObject;
import com.lele.gobang.common.ErrorCode;
import com.lele.gobang.common.R;
import com.lele.gobang.common.ResultUtils;
import com.lele.gobang.common.constants.UserConstant;
import com.lele.gobang.model.domain.User;
import com.lele.gobang.model.domain.request.UserLoginRequest;
import com.lele.gobang.model.domain.request.UserRegisterRequest;
import com.lele.gobang.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lele
 * @create 2023-01-06 10:06
 */
@Slf4j
@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    @RequestMapping("doLogin")
    public R doLogin(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户名或密码为空");
        }
        User user = userService.doLogin(username, password);
        return ResultUtils.success(user);
    }

    @RequestMapping("doRegister")
    public R doRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户名或密码或确认密码为空");
        }
        long result = userService.doRegister(username, password, checkPassword);
        return ResultUtils.success(result);
    }

    @RequestMapping("onlineCount")
    public R getOnlineCount() {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        return ResultUtils.success(userService.getOnlineUserCount());
    }

    @RequestMapping("isLogin")
    public R checkLoginStatus() {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        return ResultUtils.success();
    }

    @RequestMapping("logout")
    public R logout() {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        int result = userService.logout();
        return ResultUtils.success(result);
    }

    @RequestMapping("info")
    public R getInfo() {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        User user = userService.getLoginUserInfo();
        return ResultUtils.success(user);
    }

    @RequestMapping("pkTopThree")
    public R getPkTopThree() {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        List<User> pkTopThreeList = userService.getPkTopThree();
        return ResultUtils.success(pkTopThreeList);
    }

    @RequestMapping("pkRankList")
    public R getPkRankListPage(@RequestParam Integer id) {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        if (id == null || id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "");
        }
        JsonObject pkRankListPage = userService.getPkRankList(id);
        return ResultUtils.success(pkRankListPage.toString());
    }

    @PostMapping("/avatar/upload")
    public R upload(MultipartFile file) {
        if (!StpUtil.isLogin()) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN);
        }
        if (file == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        FileInfo fileInfo = fileStorageService.of(file)
                .setPlatform(UserConstant.PLAT_FORM)    //使用指定的存储平台
                .image(img -> img.size(1000, 1000))  //将图片大小调整到 1000*1000
                .thumbnail(th -> th.size(200, 200))  //再生成一张 200*200 的缩略图
                .upload();
        if (fileInfo == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "上传头像失败");
        }
        String url = fileInfo.getUrl();
        User user = userService.uploadAvatar(url);
        return ResultUtils.success(user);
    }
}
