package com.bitgame.game.framework.constant;

import lombok.Getter;

public enum SysCode {
    OK(0, "ok"),
    ERROR(1, "系统异常"),

    /**
     * ==============系统内部错误码==============
     **/
    noCmdCode(10, "CMD不存在"),
    badJwtTokenCode(11, "JWT Token异常"),
    alreadyLoginCode(12, "异地登陆"),
    tokenExpiredCode(13, "Token过期"),

    /**
     * ==============应用错误码==============
     */
    requestParamsMiss(20, "请求参数异常"),
    requestExecuteException(21, "应用系统异常"),
    ;

    @Getter
    private final Integer code;

    @Getter
    private final String msg;

    SysCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
