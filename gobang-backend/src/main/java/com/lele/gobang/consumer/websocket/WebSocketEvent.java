package com.lele.gobang.consumer.websocket;

import lombok.Getter;

import java.util.Optional;

/**
 * websocket - 事件
 */
@Getter
public enum WebSocketEvent {
    MATCH("match", ""),
    STOPMATCH("stopmatch", ""),
    PUT("put", ""),
    CHAT("chat", ""),
    READY("ready", ""),
    MEMBER_QUIT("memberQuit", ""),
    MASTER_QUIT("masterQuit", ""),
    GAME_QUIT("gameQuit", ""),
    BREAK("break", "这是防止switch-case为空的默认事件");
    /**
     * 事件
     */
    private final String event;

    /**
     * 描述（详情）
     */
    private final String description;

    WebSocketEvent(String event, String description) {
        this.event = event;
        this.description = description;
    }

    public static Optional<WebSocketEvent> getType(String event) {
        for (WebSocketEvent socketEvent : values()) {
            if (socketEvent.getEvent().equals(event)) {
                return Optional.of(socketEvent);
            }
        }
        return Optional.empty();
    }
}
