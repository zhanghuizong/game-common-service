package com.bitgame.game.framework.vo.feign.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请求数据格式
 * <pre>
 * {
 *      "gameNo":"10001",
 *      "requestTime":"1585703567114",
 *      "channelId":"BITGAME",
 *      "openId":"0B97A72CEACA441298A82578738F07A9",
 *      "currency":"BTC",
 *      "outOrderNo":"G23423432",
 *      "orderNo":"G15857217532162UHZO4",
 *      "amount":"100"，
 *      "awardType"：1
 * }
 * </pre>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IncreaseRequest extends DeductRequest {
    /**
     * 平台订单编号
     */
    private String orderNo;

    /**
     * 是否直扣
     */
    private Boolean notCheckDecrFlag;
}
