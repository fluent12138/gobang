package com.lele.gobang.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.google.gson.JsonObject;
import com.lele.gobang.model.domain.User;

import java.util.List;

/**
 * @author lele
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2023-01-06 09:57:07
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    User doLogin(String username, String password);

    /**
     * 用户脱敏
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注册
     *
     * @param username      用户名
     * @param password      密码
     * @param checkPassword 确认密码
     * @return 用户id
     */
    long doRegister(String username, String password, String checkPassword);

    /**
     * 获取当前系统在线用户数
     */
    long getOnlineUserCount();

    /**
     * 获取已登录用户信息
     */
    User getLoginUserInfo();

    /**
     * 注销
     */
    int logout();

    /**
     * pk榜前三
     */
    List<User> getPkTopThree();

    /**
     * 获取排行榜
     */
    JsonObject getPkRankList(Integer id);

    /**
     * 上传头像
     */
    User uploadAvatar(String url);
}
