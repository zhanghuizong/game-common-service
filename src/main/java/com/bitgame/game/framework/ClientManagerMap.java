package com.bitgame.game.framework;

import cn.hutool.core.convert.Convert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ClientManager 管理实例容器
 * <p>
 * 结构说明:<br>
 * key:gameId<br>
 * val:ClientManager
 * </p>
 */
@EqualsAndHashCode(callSuper = true)
@Component
@Slf4j
@Data
public class ClientManagerMap extends ConcurrentHashMap<String, ClientManager> {

    public ClientManager get(WebSocketSession session) {
        return super.get(Convert.toStr(session.getAttributes().get("gameId"), ""));
    }
}
