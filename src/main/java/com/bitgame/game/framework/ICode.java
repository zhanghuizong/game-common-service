package com.bitgame.game.framework;

import com.bitgame.game.framework.constant.SysCode;

public interface ICode {
    Integer getCode();

    String getMsg();

    static Code get(SysCode code) {
        return new Code(code.getCode(), code.getMsg());
    }
}
