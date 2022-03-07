package com.bitgame.game.framework.vo.feign.request;

import com.bitgame.game.framework.config.game.JavaConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonGameApiRequest extends JavaConfig {
    /**
     * 游戏编号
     */
    private String gameNo;

    /**
     * 请求时间戳
     */
    private Long requestTime = System.currentTimeMillis();

    /**
     * 渠道id（平台使用:BITGAME）
     */
    private String channelId = "BITGAME";
}
