package com.bitgame.game.framework.config.game;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("game.jwt")
@Data
public class JwtConfig {
    /**
     * Jwt 秘钥
     */
    private String secret;

    /**
     * 过期时间增加. 单位: 秒. 默认失效时间:一个小时
     */
    private Integer expireSecondIncrement = 3600;
}
