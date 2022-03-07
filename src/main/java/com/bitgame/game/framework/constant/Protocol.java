package com.bitgame.game.framework.constant;

import lombok.Getter;

public enum Protocol {
    BUSINESS("0"),    // 业务通信数据
    INIT("1"),        // 发送初始化数据
    PING("2"),        // 发送ping
    PONG("3"),        // 接受pong
    ERROR("4"),       // 错误信息
    MSG_ACK("5"),     // 消息应答
    ;

    @Getter
    private final String id;

    Protocol(String id) {
        this.id = id;
    }
}
