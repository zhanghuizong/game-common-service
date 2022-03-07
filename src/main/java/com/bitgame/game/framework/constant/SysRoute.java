package com.bitgame.game.framework.constant;

import lombok.Getter;

public enum SysRoute {
    /**
     * 跨服推送:异地登陆
     */
    ALREADY_LOGIN("alreadyLogin"),

    /**
     * 跨服推送:
     * 1. 指定用户推送
     * 2. 推送所有用户
     */
    PUBLISH("publish"),

    /**
     * 上线通知
     */
    ONLINE("online"),

    /**
     * 下线通知
     */
    OFFLINE("offline"),
    ;

    @Getter
    private final String code;

    SysRoute(String code) {
        this.code = code;
    }
}
