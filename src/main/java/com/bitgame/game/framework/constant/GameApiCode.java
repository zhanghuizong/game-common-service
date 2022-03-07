package com.bitgame.game.framework.constant;

import lombok.Getter;

public enum GameApiCode {
    OK("0000", "接口调用成功");

    @Getter
    private final String code;

    @Getter
    private final String msg;

    GameApiCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
