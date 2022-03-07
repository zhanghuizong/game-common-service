package com.bitgame.game.framework;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 错误码响应数据对象
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class Code implements ICode {
    private Integer code;

    private String msg;
}
