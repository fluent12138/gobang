package com.lele.gobang.consumer.game;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lele.gobang.consumer.websocket.WebSocketServer;
import com.lele.gobang.model.domain.User;
import com.lele.gobang.utils.JsonUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lele
 * @create 2023-01-07 15:25
 */
@Component
@Slf4j
@NoArgsConstructor
public class Game extends Thread {
    private final static int[] dx = {0, 1, 0, -1, 1, 1, -1, -1};
    private final static int[] dy = {1, 0, -1, 0, -1, 1, 1, -1};
    private final static Integer rows = 12, cols = 12;
    private final ReentrantLock lock = new ReentrantLock();
    private int[][] g;
    private Player playerA, playerB;
    private String status = "playing"; // playing -> finished
    private String loser = ""; // all 平局, A : A输, B : B输
    private Cell nextStepA = null;
    private Cell nextStepB = null;
    private Cell last = null;
    private String exceptionLoser = null;

    public Game(Integer aId, Integer bId) {
        this.g = new int[rows][cols];

        this.playerA = new Player(aId, "black", true, new ArrayList<>());
        this.playerB = new Player(bId, "white", false, new ArrayList<>());
    }

    public Player getPlayerA() {
        return this.playerA;
    }

    public Player getPlayerB() {
        return this.playerB;
    }

    public void setNextStepA(int x, int y) {
        lock.lock();
        try {
            this.nextStepA = new Cell(x, y);
        } finally {
            lock.unlock();
        }
    }

    public void clearNextStep() {
        lock.lock();
        try {
            this.nextStepA = null;
            this.nextStepB = null;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(int x, int y) {
        lock.lock();
        try {
            this.nextStepB = new Cell(x, y);
        } finally {
            lock.unlock();
        }
    }

    public int[][] getG() {
        return g;
    }

    public void createMap() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                g[r][c] = 0;
            }
        }
    }

    private void put(int x, int y, int id) {
        // 改变地图
        this.g[x][y] = id;
        // 存当前这一步信息
        last = new Cell(x, y);
        // 放置下的'子'
        if (playerA.getIsRound()) {
            playerA.set_put(x, y);
        } else if (playerB.getIsRound()) {
            playerB.set_put(x, y);
        }
        clearNextStep();
    }

    private boolean check_finished() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                for (int k = 0; k < 8; k++) {
                    if (this.g[i][j] != 0 && this.dfs(i, j, k)) {
                        log.info("finished");
                        return true;
                    }
                }
            }
        }
        log.info("continue");
        return false;
    }

    private boolean dfs(int x, int y, int d) {
        int color = this.g[x][y];
        for (int i = 0; i < 4; i++) {
            x += dx[d];
            y += dy[d];
            if (x < 0 || x >= this.rows || y < 0 || y >= this.cols || this.g[x][y] != color) {
                return false;
            }
        }
        return true;
    }


    private void sendResult() {
        JsonObject resp = new JsonObject();
        resp.addProperty("event", "result");
        resp.addProperty("loser", loser);
        sendAllMessage(resp.toString());
        saveToData();
    }

    private void saveToData() {
        if (!"all".equals(loser)) {
            updateUserRating(playerA, playerB);
        }
        updateUserPkCount(playerA, playerB);
    }

    private void updateUserPkCount(Player playerA, Player playerB) {
        User userA = WebSocketServer.userMapper.selectById(playerA.getUserId());
        User userB = WebSocketServer.userMapper.selectById(playerB.getUserId());
        userA.setPkCount(userA.getPkCount() + 1);
        userB.setPkCount(userB.getPkCount() + 1);
        WebSocketServer.userMapper.updateById(userA);
        WebSocketServer.userMapper.updateById(userB);
    }

    @Transactional
    public void updateUserRating(Player playerA, Player playerB) {
        User userA = WebSocketServer.userMapper.selectById(playerA.getUserId());
        User userB = WebSocketServer.userMapper.selectById(playerB.getUserId());

        if ("A".equals(loser)) {
            userA.setRate(userA.getRate() - 2);
            userB.setRate(userB.getRate() + 5);
        } else if ("B".equals(loser)) {
            userA.setRate(userA.getRate() + 5);
            userB.setRate(userB.getRate() - 2);
        }
        WebSocketServer.userMapper.updateById(userA);
        WebSocketServer.userMapper.updateById(userB);
    }

    private void send_put() {
        lock.lock();
        try {
            JsonObject resp = new JsonObject();
            JsonElement map = JsonUtils.getJsonElement(getG());
            resp.addProperty("event", "put");

            // 修改两个人的回合
            playerA.setIsRound(!playerA.getIsRound());
            playerB.setIsRound(!playerB.getIsRound());
            log.info("A IsRound : {}, B IsRound : {}", playerA.getIsRound(), playerB.getIsRound());
            resp.addProperty("cellsA", playerA.getSelfString());
            resp.addProperty("cellsB", playerB.getSelfString());
            resp.addProperty("x", last.x);
            resp.addProperty("y", last.y);
            resp.add("map", map);
            sendAllMessage(resp.toString());
        } finally {
            lock.unlock();
        }
    }

    private void sendAllMessage(String message) {
        if (WebSocketServer.users.get(playerA.getUserId()) != null) {
            WebSocketServer.users.get(playerA.getUserId()).sendMessage(message);
        }
        if (WebSocketServer.users.get(playerB.getUserId()) != null) {
            WebSocketServer.users.get(playerB.getUserId()).sendMessage(message);
        }
    }

    /**
     * 检查是否有人掉线
     */
    private boolean isOnline() {
        boolean aNotEmpty = ObjectUtils.isNotEmpty(WebSocketServer.users.get(playerA.getUserId()));
        boolean bNotEmpty = ObjectUtils.isNotEmpty(WebSocketServer.users.get(playerB.getUserId()));
        return aNotEmpty && bNotEmpty;
    }

    public void quitGame(Integer id) {
        if (playerA.getUserId().equals(id)) {
            exceptionLoser = "A";
        } else {
            exceptionLoser = "B";
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            // 获取下一步操作
            if (nextStep()) {
                if (playerA.getIsRound()) {
                    put(nextStepA.getX(), nextStepA.getY(), 1);
                } else if (playerB.getIsRound()) {
                    put(nextStepB.getX(), nextStepB.getY(), 2);
                }
                send_put();
                if (check_finished()) {
                    status = "finished";
                    if (playerA.getIsRound()) { // 这里的回合数已经改变了, A的回合说明结束游戏的是B
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                    sendResult();
                    break;
                }
            } else { // 如果没有得到下一步操作, 则下棋的一方盘判输
                status = "finished";
                // 正常处理
                if (playerA.getIsRound()) {
                    loser = "A";
                } else {
                    loser = "B";
                }
                // 平局
                if (checkDraw()) {
                    loser = "all";
                }
                // 掉线处理
                if (exceptionLoser != null) {
                    loser = exceptionLoser;
                }
                sendResult();
                break;
            }
        }
    }

    private void onDisconnect() {
        boolean aEmpty = ObjectUtils.isEmpty(WebSocketServer.users.get(playerA.getUserId()));
        boolean bEmpty = ObjectUtils.isEmpty(WebSocketServer.users.get(playerB.getUserId()));
        if (aEmpty && bEmpty) {
            exceptionLoser = "all";
        } else if (aEmpty) {
            exceptionLoser = "A";
        } else {
            exceptionLoser = "B";
        }
    }

    private boolean checkDraw() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (this.g[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean nextStep() {
        // 减缓服务器压力~
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 150; i++) { // 15s下棋
            if (!isOnline()) {
                onDisconnect();
                return false;
            }
            if (exceptionLoser != null) {
                return false;
            }
            // 平局
            if (checkDraw()) {
                return false;
            }
            try {
                Thread.sleep(200);
                lock.lock();
                try {
                    if (nextStepA != null && playerA.getIsRound()) {
                        return true;
                    } else if (nextStepB != null && playerB.getIsRound()) {
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
