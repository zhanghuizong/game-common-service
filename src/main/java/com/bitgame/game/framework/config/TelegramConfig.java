package com.bitgame.game.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("telegram")
@Data
public class TelegramConfig {
    /**
     * telegram 远程接口地址
     */
    private String url;

    /**
     * telegram 聊天群ID
     */
    private String chatId;

    /**
     * 报警开关. 默认:true
     */
    private Boolean isSend = true;
}
