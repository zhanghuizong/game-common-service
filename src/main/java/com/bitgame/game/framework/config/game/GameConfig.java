package com.bitgame.game.framework.config.game;

import com.bitgame.game.framework.constant.Encoder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 游戏基本配置
 */
@Configuration
@ConfigurationProperties("game")
@Data
public class GameConfig {
    /**
     * 游戏ID. 找平台申请
     */
    private String id;

    /**
     * 是否加密传输 false:无需加密传输 true:加密传输. 默认:true.
     */
    private Boolean auth = true;

    /**
     * websocket 数据传送格式 1: 文本 2:二进制.
     */
    private Integer encoder = Encoder.TEXT.getId();

    /**
     * 设置 websocket 连接协议地址
     */
    private String wsUrlPath = "/ws";

    /**
     * 异常情况下是否需要推送错误消息, 默认值: true.
     */
    private Boolean isExceptionPush = true;

    /**
     * 是否输出 websocket 请求日志, 默认值: true
     */
    private Boolean isShowRequestLog = true;

    /**
     * 是否输出 websocket 推送日志, 默认值:true
     */
    private Boolean isShowPushLog = true;

    /**
     * 是否支持多游戏部署运行. 默认:false
     */
    private Boolean isMultiGame = false;

    /**
     * Jwt 秘钥
     */
    private JwtConfig jwt;

    /**
     * 私钥和公钥
     */
    private RsaConfig rsa;

    /**
     * Java 相关配置项
     */
    private JavaConfig java;

    /**
     * 设置 controller 扫描包. 必须是:完全限定包名称. 示例: com.bitgame.game.controller
     */
    private List<String> scanPackage;
}
