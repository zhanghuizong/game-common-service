package com.bitgame.game.framework.vo.feign.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LegalsResponse {
    /**
     * 法币名称
     */
    private String legal;

    /**
     * 法币价格
     */
    private BigDecimal price;
}
