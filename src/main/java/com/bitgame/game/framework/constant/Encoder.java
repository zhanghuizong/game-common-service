package com.bitgame.game.framework.constant;

import lombok.Getter;

/**
 * 消息推送数据类型
 */
public enum Encoder {
    TEXT(1),    // 文本
    BINARY(2),  // 二进制
    ;

    @Getter
    private final Integer id;

    Encoder(Integer id) {
        this.id = id;
    }
}
