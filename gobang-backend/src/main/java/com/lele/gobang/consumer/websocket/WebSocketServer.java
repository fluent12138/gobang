package com.lele.gobang.consumer.websocket;

import cn.dev33.satoken.stp.StpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lele.gobang.consumer.chat.Chat;
import com.lele.gobang.consumer.game.Game;
import com.lele.gobang.mapper.UserMapper;
import com.lele.gobang.model.domain.Home;
import com.lele.gobang.model.domain.User;
import com.lele.gobang.service.HomeService;
import com.lele.gobang.service.MatchService;
import com.lele.gobang.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/websocket/{userid}")
public class WebSocketServer {
    public static final ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    public static UserMapper userMapper;
    public static HomeService homeService;
    private static MatchService matchService;
    private Session session = null;
    private Game game = null;
    private Chat chat = null;
    private User user;

    /**
     * 初始化房间信息
     */
    public static void initHome(String homeId, Integer aId, Integer bId) {
        log.info("home :{}, aId: {}, bId: {}", homeId, aId, bId);
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);
        Chat chat = new Chat(aId, bId);
        if (users.get(a.getId()) != null) {
            users.get(a.getId()).chat = chat;
        }
        if (users.get(b.getId()) != null) {
            users.get(b.getId()).chat = chat;
        }

        chat.start();
        JsonObject respA = new JsonObject();
        respA.addProperty("event", "initHome");
        respA.addProperty("opponent_username", b.getUsername());
        respA.addProperty("opponent_photo", b.getAvatar());
        respA.addProperty("opponent_rate", b.getRate());
        respA.addProperty("homeId", homeId);
        respA.addProperty("aId", a.getId());
        respA.addProperty("bId", b.getId());
        if (users.get(a.getId()) != null) {
            users.get(a.getId()).sendMessage(respA.toString());
        }

        JsonObject respB = new JsonObject();
        respB.addProperty("event", "initHome");
        respB.addProperty("opponent_username", a.getUsername());
        respB.addProperty("opponent_photo", a.getAvatar());
        respB.addProperty("opponent_rate", a.getRate());
        respB.addProperty("homeId", homeId);
        respB.addProperty("aId", a.getId());
        respB.addProperty("bId", b.getId());
        if (users.get(b.getId()) != null) {
            users.get(b.getId()).sendMessage(respB.toString());
        }
    }

    public static void startGame(Integer aId, Integer bId) {
        log.info("start game, a : {} - b : {}", aId, bId);
        Game game = new Game(aId, bId);

        game.createMap();
        if (users.get(aId) != null) {
            users.get(aId).game = game;
        }
        if (users.get(bId) != null) {
            users.get(bId).game = game;
        }
        game.start();

        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);
        var socketA = users.get(a.getId());
        var socketB = users.get(b.getId());

        Gson gson = new Gson();
        JsonObject respGame = new JsonObject();
        JsonElement map = JsonUtils.getJsonElement(game.getG());
        respGame.add("map", map);
        respGame.addProperty("a_id", game.getPlayerA().getUserId());
        respGame.addProperty("b_id", game.getPlayerB().getUserId());
        respGame.addProperty("a_round", game.getPlayerA().getIsRound());
        respGame.addProperty("b_round", game.getPlayerB().getIsRound());

        JsonElement jsonRespGame = JsonUtils.getJsonElement(respGame);
        JsonObject respA = new JsonObject(), respB = new JsonObject();

        respA.addProperty("event", "playing");
        respA.addProperty("opponent_username", b.getUsername());
        respA.addProperty("opponent_photo", b.getAvatar());
        respA.addProperty("opponent_rate", b.getRate());
        respA.add("game", jsonRespGame);

        if (socketA != null) {
            socketA.sendMessage(gson.toJson(respA));
        }

        respB.addProperty("event", "playing");
        respB.addProperty("opponent_username", a.getUsername());
        respB.addProperty("opponent_photo", a.getAvatar());
        respB.addProperty("opponent_rate", a.getRate());
        respB.add("game", jsonRespGame);

        if (socketB != null) {
            socketB.sendMessage(gson.toJson(respB));
        }
    }

    public static void sendHomeInfo(List<Home> homeList) {
        users.forEach((userid, socket) -> {
            JsonObject resp = new JsonObject();
            resp.addProperty("event", "home");
            resp.add("homeList", JsonUtils.getJsonElement(homeList));
            socket.sendMessage(resp.toString());
        });
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setUserMapper(HomeService homeService) {
        WebSocketServer.homeService = homeService;
    }

    @Autowired
    public void setMatchService(MatchService matchService) {
        this.matchService = matchService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userid") Integer userid) throws IOException {
        this.session = session;
        // 建立连接
        String token = StpUtil.getTokenValueByLoginId(userid);
        Object id = StpUtil.getLoginIdByToken(token);
        if (id != null) {
            users.put(userid, this);
            this.user = userMapper.selectById(userid);
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        if (this.user != null) {
            users.remove(this.user.getId());
            matchService.removePlayer(user);
            homeService.removeHomeOnSocket(user.getId());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        Gson gson = new Gson();
        JsonObject data = gson.fromJson(message, JsonObject.class);
        String event = data.get("event").getAsString();
        log.info("receive data : {} --- event : {}", data, event);
        WebSocketEvent socketEvent = WebSocketEvent.getType(event).orElse(WebSocketEvent.BREAK);

        switch (socketEvent) {
            case MATCH:
                matchService.addPlayer(user);
                break;
            case STOPMATCH:
                matchService.removePlayer(user);
                break;
            case PUT:
                put(data.get("x").getAsInt(), data.get("y").getAsInt());
                break;
            case CHAT:
                chatMsg(data.get("content").getAsString());
                break;
            case READY:
                ready(data.get("ready").getAsInt());
                break;
            case MEMBER_QUIT:
            case MASTER_QUIT:
                quit(event);
                break;
            case GAME_QUIT:
                gameQuit();
                break;
            default:
                break;
        }
    }

    private void gameQuit() {
        if (user != null) {
            game.quitGame(user.getId());
        }
    }

    private void quit(String event) {
        JsonObject resp = new JsonObject();
        resp.addProperty("event", event);
        Integer sendUserId = null;

        if (chat.getPersonA().getId().equals(user.getId())) {
            sendUserId = chat.getPersonB().getId();
            homeService.masterQuit(chat.getPersonA().getId());
        } else {
            sendUserId = chat.getPersonA().getId();
            homeService.memberQuit(sendUserId);
        }

        var socket = users.get(sendUserId);
        socket.sendMessage(resp.toString());
    }

    private void ready(Integer ready) {
        if (chat.getPersonA().getId().equals(user.getId())) {
            chat.setAready(ready);
        } else if (chat.getPersonB().getId().equals(user.getId())) {
            chat.setBready(ready);
        }
    }

    private void chatMsg(String content) {
        if (chat.getPersonA().getId().equals(user.getId())) {
            chat.setAcontent(content);
        } else if (chat.getPersonB().getId().equals(user.getId())) {
            chat.setBcontent(content);
        }
    }

    private void put(int x, int y) {
        if (game.getPlayerA().getUserId().equals(user.getId())) {
            game.setNextStepA(x, y);
        } else if (game.getPlayerB().getUserId().equals(user.getId())) {
            game.setNextStepB(x, y);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}