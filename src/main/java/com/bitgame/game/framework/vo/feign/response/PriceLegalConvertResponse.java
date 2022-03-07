package com.bitgame.game.framework.vo.feign.response;

import lombok.Data;

import java.util.List;

@Data
public class PriceLegalConvertResponse {
    /**
     * 币种名称
     */
    private String currency;

    /**
     * 币种
     */
    private List<LegalsResponse> legals;
}
