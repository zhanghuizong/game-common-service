package com.bitgame.game.framework.vo.feign.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请求数据格式
 *
 * <pre>
 * {
 *      "gameNo":"10001",
 *      "requestTime":"1585703567114",
 *      "channelId":"BITGAME",
 *      "openId":"0B97A72CEACA441298A82578738F07A9",
 *      "currency":"BTC",
 * }
 * </pre>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetBalanceRequest extends GetBalanceListRequest {
    private String currency;
}
