package com.bitgame.game.framework.vo.feign.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RollBackDecrRequest extends CommonGameApiRequest {
    /**
     * 游戏订单编号
     */
    private String outOrderNo;
}
