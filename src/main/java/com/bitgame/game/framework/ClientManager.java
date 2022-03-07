package com.bitgame.game.framework;

import com.bitgame.game.framework.constant.SysRoute;
import com.bitgame.game.framework.dao.LoginDao;
import com.bitgame.game.framework.util.WsHandlerUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端管理器
 */
@Slf4j
public final class ClientManager {
    // 客户端连接管理 socketId<=>Client
    @Getter
    private final Map<String, Client> clientMap = new ConcurrentHashMap<>();

    // 用户列表 userId<=>socketId
    @Getter
    private final Map<String, String> socketIdMap = new ConcurrentHashMap<>();

    private final LoginDao loginDao;

    public ClientManager(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    /**
     * 缓存 websocket 连接对象
     *
     * @param client websocket 连接对象
     */
    public void addClient(Client client) {
        if (client == null) {
            log.error("客户端管理器. 添加 client 发生严重错误");
            return;
        }

        String id = client.getId();
        if (id == null || "".equals(id)) {
            log.error("客户端管理器. client.id 为空严重错误");
        }

        clientMap.put(id, client);
    }

    /**
     * 获取 websocket 连接对象
     *
     * @param sessionId websocket id
     * @return Client
     */
    public Client getClient(String sessionId) {
        if (sessionId == null) {
            return null;
        }

        return clientMap.get(sessionId);
    }

    /**
     * 根据用户ID获取 websocket 连接对象
     *
     * @param userId 用户ID
     * @return Client
     */
    public Client getClientByUserId(String userId) {
        if (userId == null) {
            return null;
        }

        return getClient(getSocketId(userId));
    }

    /**
     * 删除缓存 websocket 连接对象
     *
     * @param sessionId websocket id
     */
    public void delClient(String sessionId) {
        if (sessionId == null) {
            return;
        }

        clientMap.remove(sessionId);
    }

    /**
     * 将用户ID与 Websocket 对象做个关联
     *
     * @param userId    用户ID
     * @param sessionId websocket id
     */
    public void addSocketId(String userId, String sessionId) {
        if (userId == null || sessionId == null) {
            return;
        }

        socketIdMap.put(userId, sessionId);
    }

    /**
     * 根据用户ID获取 websocket id
     *
     * @param userId 用户ID
     * @return String
     */
    public String getSocketId(String userId) {
        if (userId == null) {
            return null;
        }

        return socketIdMap.get(userId);
    }

    /**
     * 根据 userId 删除
     *
     * @param userId 用户
     */
    public void delSocketId(String userId) {
        if (userId == null) {
            return;
        }

        socketIdMap.remove(userId);
    }

    /**
     * 获取在线用户列表
     *
     * @return List<String>
     */
    public List<String> getUsers() {
        return new ArrayList<>(socketIdMap.keySet());
    }

    /**
     * 用户离线清理数据
     *
     * @param sessionId 会话ID
     */
    public void closeClient(String sessionId) {
        try {
            Client client = removeClient(sessionId);
            if (client != null) {
                client.close();
            }
        } catch (Exception e) {
            log.error("关闭websocket通道发生异常.", e);
        }
    }

    /**
     * 用户离线清理数据
     *
     * @param sessionId 会话ID
     */
    public Client removeClient(String sessionId) {
        try {
            Client client = getClient(sessionId);
            if (client == null) {
                return null;
            }

            String gameId = client.getGameId();
            String userId = client.getUserId();
            delSocketId(userId);
            delClient(sessionId);

            // 通知离线
            WsHandlerUtil.callRoute(client, SysRoute.OFFLINE.getCode());

            // 获取缓存中的 socketId
            String cacheSession = loginDao.getSocketId(gameId, userId);

            // 判断本服务器是否有删除缓存权限，不能删除说明：该缓存谁写入的谁删除
            if (sessionId.equals(cacheSession)) {
                loginDao.delSocketId(gameId, userId);
            }

            return client;
        } catch (Exception e) {
            log.error("用户离线清理数据发生异常.", e);
        }

        return null;
    }
}
