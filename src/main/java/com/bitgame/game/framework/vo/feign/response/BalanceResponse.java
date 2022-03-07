package com.bitgame.game.framework.vo.feign.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceResponse {
    private BigDecimal total;

    private BigDecimal totalAvailable;

    private BigDecimal available;

    private BigDecimal frozen;

    private BigDecimal worthUSDT;

    private Integer sort;
}
