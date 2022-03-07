package com.bitgame.game.framework.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RedisChannelModel implements Serializable {
    /**
     * 游戏ID
     */
    private String gameId;

    /**
     * 广播消息类型
     * 配置：
     */
    private String msgType;

    /**
     * 发送消息主机名
     */
    private String hostname;

    /**
     * 推送消息用户
     */
    private String[] users;

    /**
     * 广播数据
     */
    private Object data;
}
