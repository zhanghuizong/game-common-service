package com.bitgame.game.framework.interceptor;

import com.bitgame.game.framework.ClientManager;
import com.bitgame.game.framework.ClientManagerMap;
import com.bitgame.game.framework.config.game.GameConfig;
import com.bitgame.game.framework.config.redis.listener.RedisMessageListener;
import com.bitgame.game.framework.constant.RedisConstant;
import com.bitgame.game.framework.dao.LoginDao;
import com.bitgame.game.framework.util.BaseUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * WebSocket 拦截器
 * <p>
 * 主要目的解析客户端传递参数 commonId
 * </p>
 */
@Component
@Slf4j
public class ConnectInterceptor extends InterceptorAbstract {
    @Resource
    private ClientManagerMap clientManagerMap;

    @Resource
    private LoginDao loginDao;

    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Resource
    private RedisMessageListener redisMessageListener;

    public ConnectInterceptor(GameConfig gameConfig) {
        super(gameConfig);
    }

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes
    ) throws Exception {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String gameId = setGameId(servletRequest);
        String commonId = getCommonId(servletRequest);

        attributes.put("gameId", gameId);       // 注入 gameId 参数
        attributes.put("commonId", commonId);   // 注入 commonId 参数

        // 实例化: ClientManager, Redis监听器
        // 必须确保: 每个游戏在每台服务器中只能被实例化一次
        if (clientManagerMap.get(gameId) == null) {
            synchronized (ConnectInterceptor.class) {
                if (clientManagerMap.get(gameId) == null) {
                    // 实例化游戏: websocket 实例对象管理器
                    clientManagerMap.put(gameId, new ClientManager(loginDao));

                    // 实例化游戏: Redis 订阅通道
                    redisMessageListenerContainer.addMessageListener(
                            redisMessageListener,
                            new ChannelTopic(BaseUtil.getKey(gameId, RedisConstant.CHANNEL_COMMUNICATION))
                    );
                }
            }
        }

        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
