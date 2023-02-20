package com.lele.gobang.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import com.lele.gobang.common.ErrorCode;
import com.lele.gobang.common.constants.UserConstant;
import com.lele.gobang.consumer.websocket.WebSocketServer;
import com.lele.gobang.exception.BusinessException;
import com.lele.gobang.mapper.HomeMapper;
import com.lele.gobang.model.domain.Home;
import com.lele.gobang.service.HomeService;
import com.lele.gobang.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * @author 勒勒
 * @description 针对表【home(游戏房间表)】的数据库操作Service实现
 * @createDate 2023-01-10 15:39:55
 */
@Service
@Slf4j
public class HomeServiceImpl extends ServiceImpl<HomeMapper, Home>
        implements HomeService {
    @Override
    public Home createHome(String password) {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        // 判断是否已经创建房间
        int userid = StpUtil.getLoginIdAsInt();
        QueryWrapper<Home> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("a_id", userid);
        Home exist = this.baseMapper.selectOne(queryWrapper);
        if (exist != null) {
            throw new BusinessException(ErrorCode.NO_AUTH, "你已经创建了房间");
        }
        // 设置房间号
        UUID uuid = UUID.randomUUID();
        String homeId = StringUtils.substring(uuid.toString(), 0, 8);
        String encryptPassword = null;
        // 加密密码
        if (!StringUtils.isBlank(password)) {
            encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + password).getBytes());
        }
        Home home = Home.builder()
                .aId(userid)
                .homeId(homeId)
                .password(encryptPassword)
                .build();
        int insert = this.baseMapper.insert(home);
        if (insert <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        sendMessage();
        return home;
    }

    @Override
    public int removeHome() {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int userid = StpUtil.getLoginIdAsInt();
        QueryWrapper<Home> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("a_id", userid);
        int remove = this.baseMapper.delete(queryWrapper);
        sendMessage();
        return remove;
    }

    @Override
    public void removeHomeOnSocket(Integer id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        QueryWrapper<Home> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("a_id", id);
        this.baseMapper.delete(queryWrapper);
        sendMessage();
    }

    @Override
    public int joinHome(String homeId, String password) {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int userid = StpUtil.getLoginIdAsInt();
        QueryWrapper<Home> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("home_id", homeId);
        Home home = this.baseMapper.selectOne(queryWrapper);
        // 为空的校验
        if (home == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "房间号错误或密码错误");
        }
        // 已经有人抢先一步
        if (home.getBId() != null && home.getBId() != -1) {
            throw new BusinessException(ErrorCode.NO_AUTH, "你晚来了一步哦");
        }
        // 密码校验
        if (StringUtils.isNotEmpty(home.getPassword())) {
            String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + password).getBytes());
            if (!home.getPassword().equals(encryptPassword)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "房间号错误或密码错误");
            }
        }
        // 自己不能加入自己的房间
        if (home.getAId().equals(userid)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "你已经是这个房间的主人");
        }
        home.setBId(userid);
        int update = this.baseMapper.updateById(home);
        if (update <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        sendMessage();
        WebSocketServer.initHome(homeId, home.getAId(), home.getBId());
        return update;
    }

    @Override
    public Home searchByHomeId(String homeId) {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (homeId == null || StringUtils.isBlank(homeId)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "房间号不能为空");
        }
        QueryWrapper<Home> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("home_id", homeId);
        Home home = this.baseMapper.selectOne(queryWrapper);
        if (home == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "房间号错误");
        }
        return home;
    }

    @Override
    public JsonObject page(Integer page) {
        IPage<Home> userPage = new Page<>(page, 7);
        QueryWrapper<Home> query = new QueryWrapper<>();
        query.orderByDesc("create_time");
        List<Home> homes = this.baseMapper.selectPage(userPage, query).getRecords();
        JsonObject resp = new JsonObject();
        resp.add("homes", JsonUtils.getJsonElement(homes));
        Long count = this.baseMapper.selectCount(null);
        resp.addProperty("count", count);
        return resp;
    }

    @Override
    public void memberQuit(Integer id) {
        log.info("id : {}", id);
        if (id == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        QueryWrapper<Home> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("a_id", id);
        Home home = this.baseMapper.selectOne(queryWrapper);
        if (home == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        home.setBId(-1);
        log.info("home : {}", home);
        int update = this.baseMapper.updateById(home);
        if (update <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        sendMessage();
    }

    @Override
    public void masterQuit(Integer id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        QueryWrapper<Home> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("a_id", id);
        int delete = this.baseMapper.delete(queryWrapper);
        if (delete <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        sendMessage();
    }

    private void sendMessage() {
        QueryWrapper<Home> query = new QueryWrapper<>();
        query.orderByDesc("create_time").last("limit 7");
        List<Home> homeList = this.baseMapper.selectList(query);
        WebSocketServer.sendHomeInfo(homeList);
    }
}




