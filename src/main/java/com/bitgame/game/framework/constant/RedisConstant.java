package com.bitgame.game.framework.constant;

/**
 * 系统内部 Redis Key 集合
 */
public interface RedisConstant {
    /**
     * 单点登陆用户
     */
    String LOGIN_USERS = "bitgame:single:login:users";

    /**
     * Redis 订阅频道
     */
    String CHANNEL_COMMUNICATION = "bitgame:channel:communication";
}
