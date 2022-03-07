package com.bitgame.game.framework.vo.feign.request;

import lombok.Data;

@Data
public class SignRequest {
    /**
     * RSA密文
     */
    private String sign;
}
