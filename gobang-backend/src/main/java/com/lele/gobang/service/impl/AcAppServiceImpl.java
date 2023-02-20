package com.lele.gobang.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lele.gobang.common.constants.LifeConstant;
import com.lele.gobang.common.constants.UserConstant;
import com.lele.gobang.mapper.LifeUserMapper;
import com.lele.gobang.mapper.UserMapper;
import com.lele.gobang.model.domain.LifeUser;
import com.lele.gobang.model.domain.User;
import com.lele.gobang.service.AcAppService;
import com.lele.gobang.service.OnePutLevelService;
import com.lele.gobang.utils.HttpClientUtil;
import com.lele.gobang.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

/**
 * @author lele
 * @create 2022-10-09 20:30
 */
@Service
@AllArgsConstructor
@Slf4j
public class AcAppServiceImpl implements AcAppService {
    private final RedisTemplate<String, String> redisTemplate;
    private final UserMapper userMapper;
    private final LifeUserMapper lifeUserMapper;
    private final OnePutLevelService onePutLevelService;

    @Override
    public JsonObject applyCode() {
        log.info("apply code");
        JsonObject resp = new JsonObject();
        resp.addProperty("appid", UserConstant.APPID);
        try {
            resp.addProperty("redirect_uri", URLEncoder.encode(UserConstant.ACAPP_REDIRECT_URI, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            resp.addProperty("result", "failed");
            return resp;
        }
        resp.addProperty("scope", "userinfo");
        StringBuilder state = getState();
        resp.addProperty("state", state.toString());
        resp.addProperty("result", "success");
        redisTemplate.opsForValue().set(state.toString(), "true");
        redisTemplate.expire(state.toString(), Duration.ofMinutes(10));
        log.info("redis add key : " + state);
        return resp;
    }

    @Override
    public JsonObject applyCodeForLife() {
        log.info("apply life code");
        JsonObject resp = new JsonObject();
        resp.addProperty("appid", LifeConstant.APPID);
        try {
            resp.addProperty("redirect_uri", URLEncoder.encode(LifeConstant.ACAPP_REDIRECT_URI, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            resp.addProperty("result", "failed");
            return resp;
        }
        resp.addProperty("scope", "userinfo");
        StringBuilder state = getState();
        resp.addProperty("state", state.toString());
        resp.addProperty("result", "success");
        redisTemplate.opsForValue().set(state.toString(), "true");
        redisTemplate.expire(state.toString(), Duration.ofMinutes(10));
        log.info("redis add key : " + state);
        return resp;
    }

    /**
     * 接收第三方返回结果
     */
    @Override
    public JsonObject receiveCode(String code, String state) {
        // 处理acwing返回信息
        JsonObject resp = new JsonObject();
        Gson gson = new Gson();
        resp.addProperty("result", "failed");
        if (code == null || state == null) {
            log.info("code or state is null");
            return resp;
        }
        if (Boolean.FALSE.equals(redisTemplate.hasKey(state))) {
            log.info("redis has not state" + state);
            return resp;
        }
        redisTemplate.delete(state);

        // 申请token
        List<NameValuePair> nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("appid", UserConstant.APPID));
        nameValuePairs.add(new BasicNameValuePair("secret", UserConstant.APPSECRET));
        nameValuePairs.add(new BasicNameValuePair("code", code));
        String getString = HttpClientUtil.get(UserConstant.APPLY_ACWING_ACCESS_TOKEN_URL, nameValuePairs);
        if (getString == null) {
            log.info("apply token false");
            return resp;
        }

        JsonObject getResp = gson.fromJson(getString, JsonObject.class);
        String accessToken = getResp.get("access_token").getAsString();
        String openid = getResp.get("openid").getAsString();
        if (accessToken == null || openid == null) {
            log.info("accessToken or openid is null");
            return resp;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid", openid);
        List<User> users = userMapper.selectList(queryWrapper);

        if (!users.isEmpty()) {
            User user = users.get(0);
            JsonElement userJson = JsonUtils.getJsonElement(user);
            StpUtil.login(user.getId());
            resp.addProperty("result", "success");
            resp.add("user", userJson);
            resp.add("token", JsonUtils.getJsonElement(StpUtil.getTokenInfo()));
            onePutLevelService.initOnePut(user.getId());
            return resp;
        }

        // 不存在该用户, 继续申请用户信息
        nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("access_token", accessToken));
        nameValuePairs.add(new BasicNameValuePair("openid", openid));
        getString = HttpClientUtil.get(UserConstant.APPLY_ACWING_USERINFO_URL, nameValuePairs);
        if (getString == null) {
            log.info("apply info false");
            return resp;
        }
        getResp = gson.fromJson(getString, JsonObject.class);
        String username = getResp.get("username").getAsString();
        String photo = getResp.get("photo").getAsString();

        if (username == null || photo == null) {
            log.error("username or photo is null");
            return resp;
        }
        // 处理用户名重复
        for (int i = 0; i < 100; i++) {
            QueryWrapper<User> query = new QueryWrapper<>();
            query.eq("username", username);
            if (userMapper.selectList(query).isEmpty()) {
                break;
            }
            username += (char) (Math.random() * 10 + '0');
            if (i == 99) {
                return resp;
            }
        }
        User user = User
                .builder()
                .username(username)
                .password(null)
                .avatar(photo)
                .rate(UserConstant.DEFAULT_RATE)
                .openid(openid)
                .build();
        userMapper.insert(user);

        User loginUser = userMapper.selectOne(queryWrapper);
        StpUtil.login(loginUser.getId());
        onePutLevelService.initOnePut(loginUser.getId());
        resp.add("user", JsonUtils.getJsonElement(user));
        resp.addProperty("result", "success");
        resp.add("token", JsonUtils.getJsonElement(StpUtil.getTokenInfo()));
        return resp;
    }

    @Override
    public JsonObject receiveCodeForLife(String code, String state) {
        // 处理acwing返回信息
        JsonObject resp = new JsonObject();
        Gson gson = new Gson();
        resp.addProperty("result", "failed");
        if (code == null || state == null) {
            log.info("code or state is null");
            return resp;
        }
        if (Boolean.FALSE.equals(redisTemplate.hasKey(state))) {
            log.info("redis has not state" + state);
            return resp;
        }
        redisTemplate.delete(state);

        // 申请token
        List<NameValuePair> nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("appid", LifeConstant.APPID));
        nameValuePairs.add(new BasicNameValuePair("secret", LifeConstant.APPSECRET));
        nameValuePairs.add(new BasicNameValuePair("code", code));
        String getString = HttpClientUtil.get(LifeConstant.APPLY_ACWING_ACCESS_TOKEN_URL, nameValuePairs);
        if (getString == null) {
            log.info("apply token false");
            return resp;
        }

        JsonObject getResp = gson.fromJson(getString, JsonObject.class);
        String accessToken = getResp.get("access_token").getAsString();
        String openid = getResp.get("openid").getAsString();
        if (accessToken == null || openid == null) {
            log.info("accessToken or openid is null");
            return resp;
        }
        QueryWrapper<LifeUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid", openid);

        List<LifeUser> users = lifeUserMapper.selectList(queryWrapper);

        if (!users.isEmpty()) {
            LifeUser user = users.get(0);
            JsonElement userJson = JsonUtils.getJsonElement(user);
            resp.addProperty("result", "success");
            resp.add("user", userJson);
            return resp;
        }

        // 不存在该用户, 继续申请用户信息
        nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("access_token", accessToken));
        nameValuePairs.add(new BasicNameValuePair("openid", openid));
        getString = HttpClientUtil.get(LifeConstant.APPLY_ACWING_USERINFO_URL, nameValuePairs);
        if (getString == null) {
            log.info("apply info false");
            return resp;
        }
        getResp = gson.fromJson(getString, JsonObject.class);
        String username = getResp.get("username").getAsString();
        String photo = getResp.get("photo").getAsString();

        if (username == null || photo == null) {
            log.error("username or photo is null");
            return resp;
        }
        // 处理用户名重复
        for (int i = 0; i < 100; i++) {
            QueryWrapper<LifeUser> query = new QueryWrapper<>();
            query.eq("username", username);
            if (lifeUserMapper.selectList(query).isEmpty()) {
                break;
            }
            username += (char) (Math.random() * 10 + '0');
            if (i == 99) {
                return resp;
            }
        }
        LifeUser user = LifeUser
                .builder()
                .username(username)
                .avatar(photo)
                .openid(openid)
                .accurate(LifeConstant.DEFAULT_ACCURATE)
                .life(LifeConstant.DEFAULT_LIFE)
                .tension(LifeConstant.DEFAULT_TENSION)
                .loving(LifeConstant.DEFAULT_LOVING)
                .lovestart(LifeConstant.DEFAULT_LOVING_START)
                .birthday(LifeConstant.DEFAULT_BIRTHDAY)
                .build();
        lifeUserMapper.insert(user);

        resp.add("user", JsonUtils.getJsonElement(user));
        resp.addProperty("result", "success");
        return resp;
    }

    private StringBuilder getState() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            res.append((char) (Math.random() * 10 + '0'));
        }
        return res;
    }
}
