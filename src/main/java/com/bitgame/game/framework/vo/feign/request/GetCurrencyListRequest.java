package com.bitgame.game.framework.vo.feign.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请求数据格式
 * <pre>
 *
 * 	    "gameNo": "10001",
 * 	    "requestTime": "1585703567114",
 * 	    "channelId": "BITGAME"
 * }
 * </pre>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetCurrencyListRequest extends CommonGameApiRequest {
    /**
     * 游戏一级域名（示例：bitgame.com）
     */
    private String domainKey;
}
