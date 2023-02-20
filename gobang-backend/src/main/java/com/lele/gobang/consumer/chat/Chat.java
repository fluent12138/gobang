package com.lele.gobang.consumer.chat;

import com.google.gson.JsonObject;
import com.lele.gobang.consumer.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lele
 * @create 2022-09-19 12:36
 */
@Slf4j
public class Chat extends Thread {
    private final ReentrantLock lock1 = new ReentrantLock();
    private final ReentrantLock lock2 = new ReentrantLock();
    private final Person A;
    private final Person B;

    public Chat(Integer aId, Integer bId) {
        A = new Person(aId, null, 0);
        B = new Person(bId, null, 0);
    }

    public Person getPersonA() {
        return this.A;
    }

    public Person getPersonB() {
        return this.B;
    }


    public void setAready(Integer ready) {
        lock2.lock();
        try {
            this.A.setReady(ready);
            sendReady(ready, B.getId());
        } finally {
            lock2.unlock();
        }
    }

    public void setBready(Integer ready) {
        lock2.lock();
        try {
            this.B.setReady(ready);
            sendReady(ready, A.getId());
        } finally {
            lock2.unlock();
        }
    }

    public void setAcontent(String content) {
        lock1.lock();
        try {
            this.A.setContent(content);
        } finally {
            lock1.unlock();
        }
    }

    public void setBcontent(String content) {
        lock1.lock();
        try {
            this.B.setContent(content);
        } finally {
            lock1.unlock();
        }
    }

    private boolean checkChat() {
        return !StringUtils.isAllBlank(A.getContent(), B.getContent());
    }

    private boolean checkReady() {
        return A.getReady() == 1 && B.getReady() == 1;
    }

    /**
     * 检查是否有人掉线
     */
    private boolean isOnline() {
        return ObjectUtils.isNotEmpty(WebSocketServer.users.get(A.getId())) &&
                ObjectUtils.isNotEmpty(WebSocketServer.users.get(B.getId()));
    }

    private void send(String content, Integer id) {
        log.info(content);
        if (StringUtils.isBlank(content)) {
            return;
        }
        JsonObject resp = new JsonObject();
        resp.addProperty("event", "content");
        resp.addProperty("content", content);
        sendMessage(id, resp.toString());
    }

    private void sendReady(Integer ready, Integer id) {
        JsonObject resp = new JsonObject();
        resp.addProperty("event", "ready");
        if (ready == 1) {
            resp.addProperty("ready", "已准备");
        } else if (ready == 0) {
            resp.addProperty("ready", "未准备");
        }
        sendMessage(id, resp.toString());
    }

    private void sendMessage(Integer id, String msg) {
        if (WebSocketServer.users.get(id) != null) {
            WebSocketServer.users.get(id).sendMessage(msg);
        }
    }

    private void sendAllMessage(String message) {
        if (WebSocketServer.users.get(A.getId()) != null) {
            WebSocketServer.users.get(A.getId()).sendMessage(message);
        }
        if (WebSocketServer.users.get(B.getId()) != null) {
            WebSocketServer.users.get(B.getId()).sendMessage(message);
        }
    }

    private void sendDisconnect() {
        quitHome();
        JsonObject resp = new JsonObject();
        resp.addProperty("event", "chatDisconnect");
        sendAllMessage(resp.toString());
    }

    private void quitHome() {
        if (ObjectUtils.isEmpty(WebSocketServer.users.get(B.getId()))) {
            log.info("member quit");
            WebSocketServer.homeService.memberQuit(A.getId());
        } else if (ObjectUtils.isEmpty(WebSocketServer.users.get(A.getId()))) {
            log.info("master quit");
            WebSocketServer.homeService.masterQuit(A.getId());
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!isOnline()) {
                log.info("disconnect");
                sendDisconnect();
                break;
            }
            if (checkChat()) {
                log.info("check chat");
                lock1.lock();
                try {
                    send(A.getContent(), B.getId());
                    send(B.getContent(), A.getId());
                    A.setContent(null);
                    B.setContent(null);
                } finally {
                    lock1.unlock();
                }
            }
            if (checkReady()) {
                lock2.lock();
                try {
                    WebSocketServer.startGame(A.getId(), B.getId());
                    WebSocketServer.homeService.removeHomeOnSocket(A.getId());
                } finally {
                    lock2.unlock();
                }
                break;
            }
        }
    }
}
