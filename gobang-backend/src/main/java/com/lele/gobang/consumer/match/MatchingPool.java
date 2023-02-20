package com.lele.gobang.consumer.match;

import com.lele.gobang.consumer.game.Player;
import com.lele.gobang.consumer.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class MatchingPool extends Thread {

    private static List<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();

    public void addPlayer(Integer userId, Integer rating) {
        lock.lock();
        try {
            players.add(new Player(userId, rating));
            log.info("players : {}", players);
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId) {
        lock.lock();
        try {
            List<Player> newPlayers = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getUserId() != userId) {
                    newPlayers.add(players.get(i));
                }
            }
            players = newPlayers;
            log.info("players : {}", players);
        } finally {
            lock.unlock();
        }
    }

    private void increaseWaitingTime() {
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    /**
     * 判断是否匹配
     * 匹配策略: 分差 <= 等待时间*10, 等待时间 : 两人间等待时间较少的
     */
    private boolean checkMatched(Player a, Player b) {
        if (a.getUserId().equals(b.getUserId())) {
            return false;
        }

        int ratingDelta = Math.abs(a.getRate() - b.getRate());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }

    private void matchPlayers() { // 匹配所有玩家
        boolean[] used = new boolean[players.size()];
        int idx = -1;
        for (int i = 0; i < players.size(); i++) {
            if (used[i]) {
                continue;
            }
            for (int j = i + 1; j < players.size(); j++) {
                if (used[j]) {
                    continue;
                }
                Player a = players.get(i), b = players.get(j);
                if (checkMatched(a, b)) {
                    used[i] = used[j] = true;
                    startGame(a, b);
                    break;
                }
            }
        }

        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (!used[i]) {
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }

    private void startGame(Player a, Player b) { // 返回匹配结果
        log.info("match success: [a : {} - b : {}]", a, b);
        var aId = a.getUserId();
        var bId = b.getUserId();
        WebSocketServer.startGame(aId, bId);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}