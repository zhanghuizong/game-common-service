package com.bitgame.game.framework.vo.feign.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PriceLegalConvertRequest extends CommonGameApiRequest {
    /**
     * 法币(多个以,分割)
     */
    private String legals;

    /**
     * 数字货币(多个以,分割)
     */
    private String currencys;
}
