package com.bitgame.game.framework.vo.feign.response;

import lombok.Data;

@Data
public class GameResponse<T> {
    private String rspCode;

    private String message;

    private T data;

    private Long rspTime;
}
