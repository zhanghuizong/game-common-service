package com.bitgame.game.framework.config.game;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("game.java")
@Data
public class JavaConfig {
    /**
     * 客户端ID
     */
    @JSONField(serialize = false)
    private String clientId;

    /**
     * 客户端授权秘钥
     */
    @JSONField(serialize = false)
    private String clientSecret;

    /**
     * 接口请求密钥
     */
    @JSONField(serialize = false)
    private String apiKey;

    /**
     * 公钥
     */
    @JSONField(serialize = false)
    private String publicKey;

    /**
     * 渠道ID
     */
    private String channelId = "BITGAME";

    /**
     * 游戏一级域名
     */
    private String domainKey = "bitgame.com";
}
