package com.bitgame.game.framework.vo.feign.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请求数据格式
 * <pre>
 *
 * {
 *          "gameNo":"10001",
 *          "requestTime":"1585703567114",
 *          "channelId":"BITGAME",
 *          "openId":"0B97A72CEACA441298A82578738F07A9",
 *          "currency":"BTC",
 *          "outOrderNo":"G23423432",
 *          "amount":"100"
 * }
 *
 * </pre>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeductRequest extends CommonGameApiRequest {
    /**
     * 用户id
     */
    private String openId;

    /**
     * 货币名称
     */
    private String currency;

    /**
     * 游戏订单编号
     */
    private String outOrderNo;

    /**
     * 扣减金额
     */
    protected String amount;
}
