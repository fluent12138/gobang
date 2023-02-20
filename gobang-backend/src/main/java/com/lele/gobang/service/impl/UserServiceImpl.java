package com.lele.gobang.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lele.gobang.common.ErrorCode;
import com.lele.gobang.common.constants.UserConstant;
import com.lele.gobang.exception.BusinessException;
import com.lele.gobang.mapper.UserMapper;
import com.lele.gobang.model.domain.User;
import com.lele.gobang.service.OnePutLevelService;
import com.lele.gobang.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author 勒勒
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-01-06 09:57:07
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    private final OnePutLevelService onePutLevelService;
    private final FileStorageService fileStorageService;

    @Override
    public User doLogin(String username, String password) {
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码为空");
        }
        if (username.length() < 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名长度过短");
        }
        if (password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度过短");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + password).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码错误");
        }
        // 3. 用户脱敏
        User safetyUser = getSafetyUser(user);
        StpUtil.login(safetyUser.getId());

        onePutLevelService.initOnePut(user.getId());
        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setAvatar(originUser.getAvatar());
        safetyUser.setRate(originUser.getRate());
        safetyUser.setPkCount(originUser.getPkCount());
        safetyUser.setIsOnline(originUser.getIsOnline());
        safetyUser.setRole(originUser.getRole());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setOpenid(originUser.getOpenid());
        return safetyUser;
    }

    @Override
    public long doRegister(String username, String password, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (username.length() < 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (password.length() < 6 || checkPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码或确认密码过短");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }
        // 密码和校验密码相同
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + password).getBytes());
        // 3. 插入数据
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptPassword);
        user.setAvatar(UserConstant.DEFAULT_AVATAR_URL);
        user.setRate(UserConstant.DEFAULT_RATE);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        // 初始化oneput模式下的游戏信息
        onePutLevelService.initOnePut(user.getId());
        return user.getId();
    }

    @Override
    public User uploadAvatar(String url) {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int userId = StpUtil.getLoginIdAsInt();
        User user = this.baseMapper.selectById(userId);
        FileInfo fileInfo = fileStorageService.getFileInfoByUrl(user.getAvatar());
        if (fileInfo != null) {
            boolean delete = fileStorageService.delete(fileInfo);
            if (!delete) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
        user.setAvatar(url);
        this.baseMapper.updateById(user);
        return user;
    }

    @Override
    public long getOnlineUserCount() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("id", 0);
        List<User> userList = this.baseMapper.selectList(queryWrapper);
        userList = userList.stream().filter(user -> {
            String token = StpUtil.getTokenValueByLoginId(user.getId());
            Object id = StpUtil.getLoginIdByToken(token);
            return !Objects.isNull(id);
        }).collect(Collectors.toList());
        return userList.size();
    }

    @Override
    public User getLoginUserInfo() {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int userId = StpUtil.getLoginIdAsInt();
        User user = this.baseMapper.selectById(userId);
        return getSafetyUser(user);
    }

    @Override
    public int logout() {
        StpUtil.logout();
        return 1;
    }

    @Override
    public List<User> getPkTopThree() {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rate").last("limit 3");
        return this.baseMapper.selectList(queryWrapper).stream().map(this::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public JsonObject getPkRankList(Integer id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<JsonObject> users = new ArrayList<>();
        User me = this.baseMapper.selectById(id);
        users.add(convert(me));

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rate").last("limit 10");

        this.baseMapper.selectList(queryWrapper)
                .stream()
                .map(user -> {
                    users.add(convert(user));
                    return getSafetyUser(user);
                }).collect(Collectors.toList());

        Gson gson = new Gson();
        JsonObject resp = new JsonObject();
        JsonElement usersJson = gson.toJsonTree(users);
        resp.add("users", usersJson);
        return resp;
    }

    @Transactional
    public JsonObject convert(User user) {
        JsonObject info = new JsonObject();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("rate", user.getRate());
        Long rank = this.baseMapper.selectCount(queryWrapper) + 1;

        info.addProperty("username", user.getUsername());
        info.addProperty("avatar", user.getAvatar());
        info.addProperty("rate", user.getRate());
        info.addProperty("rank", rank);
        return info;
    }
}




