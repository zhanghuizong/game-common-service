package com.bitgame.game.framework.vo.feign.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyResponse {
    private String currency;

    private Integer sort;

    private Integer degree;

    private Integer hide;

    private BigDecimal bettingMax;

    private BigDecimal bettingMin;

    private BigDecimal operatingRange;

    private String imageUrl;

    private String svgUrl;
}
