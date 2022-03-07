package com.bitgame.game.framework.config.redis;

import com.bitgame.game.framework.Client;
import com.bitgame.game.framework.constant.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * websocket 协议路由
 */
@Slf4j
@Component
public final class RedisDispatcher {
    /**
     * 异地登陆
     *
     * @param client websocket 连接对象
     */
    public void alreadyLogin(Client client) {
        log.info("异地登陆. userId:{}, socketId:{}", client.getUserId(), client.getId());

        client.fail(SysCode.alreadyLoginCode);
        client.close();
    }

    /**
     * 跨服务器通讯 - 消息推送
     *
     * @param client websocket 连接对象
     * @param params 请求参数
     */
    public void publish(Client client, String params) {
        log.info("跨服务器消息推送. userId:{}, socketId:{}, params:{}", client.getUserId(), client.getId(), params);

        if ("".equals(params) || params == null) {
            return;
        }

        client.pushOrigin(params);
    }
}
