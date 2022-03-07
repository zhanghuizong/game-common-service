package com.bitgame.game.framework.config;

import com.bitgame.game.framework.config.game.GameConfig;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;

@Slf4j
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    @Resource
    private GameConfig gameConfig;

    @Resource
    private WebSocketHandler webSocketHandler;

    @Resource
    private HandshakeInterceptor handshakeInterceptor;

    /**
     * 注册 websocket 协议
     *
     * @param registry void
     */
    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        String wsUrlPath = gameConfig.getWsUrlPath();
        if ("".equals(wsUrlPath) || wsUrlPath == null) {
            wsUrlPath = "/ws";
        }

        registry.addHandler(webSocketHandler, wsUrlPath).setAllowedOrigins("*").addInterceptors(handshakeInterceptor);
    }
}
