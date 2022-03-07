package com.bitgame.game.framework.config.game;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("game.rsa")
@Data
public class RsaConfig {
    /**
     * 游戏 websocket 通讯私钥
     */
    private String privateKey;

    /**
     * 游戏 websocket 通讯公钥
     */
    private String publicKey;
}
