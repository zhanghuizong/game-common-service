package com.bitgame.game.framework.dao;

import com.bitgame.game.framework.Client;
import com.bitgame.game.framework.ClientManager;
import com.bitgame.game.framework.constant.RedisConstant;
import com.bitgame.game.framework.constant.SysCode;
import com.bitgame.game.framework.constant.SysRoute;
import com.bitgame.game.framework.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public final class LoginDao {
    @Resource
    private PublishDao publishDao;

    /**
     * 获取当前用户绑定的 socketId
     *
     * @param userId 用户ID
     * @return String
     */
    public String getSocketId(String gameId, String userId) {
        if (userId == null) {
            return "";
        }

        Object result = RedisUtil.hGet(gameId, RedisConstant.LOGIN_USERS, userId);
        if (result == null) {
            return "";
        }

        return result.toString();
    }

    /**
     * 绑定登陆用户与 socketId 关系
     *
     * @param userId   用户iD
     * @param socketId websocket 连接ID
     */
    public void addSocketId(String gameId, String userId, String socketId) {
        RedisUtil.hSet(gameId, RedisConstant.LOGIN_USERS, userId, socketId);
    }

    /**
     * 删除 userId 与 socketId 绑定关系
     *
     * @param userId 用户ID
     * @return Long
     */
    public Long delSocketId(String gameId, String userId) {
        return RedisUtil.hDel(gameId, RedisConstant.LOGIN_USERS, userId);
    }

    /**
     * 检测是否存在单点登陆
     *
     * @param clientManager websocket 管理器
     * @param client        websocket 连接对象
     */
    public void checkAlreadyLogin(ClientManager clientManager, Client client) {
        // Redis 没有找到当前用户绑定 socketId
        String gameId = client.getGameId();
        String userId = client.getUserId();
        String socketId = getSocketId(gameId, userId);
        if (socketId == null || "".equals(socketId)) {
            return;
        }

        // 获取 socketId 的实例对象
        Client oldClient = clientManager.getClient(socketId);
        if (oldClient != null) {
            log.warn("用户异地登陆. userId:{}, socketId:{}, sid:{}", userId, socketId, oldClient.getId());
            oldClient.fail(SysCode.alreadyLoginCode);
            oldClient.close();
            return;
        }

        // 当前已登录，但是本服务器没有找到 socketId 实例对象。需要通过 广播 给其它服务器处理
        String[] users = {client.getUserId()};
        publishDao.publish(gameId, users, SysRoute.ALREADY_LOGIN.getCode());
    }

    /**
     * 绑定 userId 与 socketId 关系
     *
     * @param clientManager websocket 管理器
     * @param client        websocket 连接对象
     */
    public void bingSocketId(ClientManager clientManager, Client client) {
        String gameId = client.getGameId();
        String userId = client.getUserId();

        // 本服务器绑定 userId 与 socketId 关系
        clientManager.addSocketId(userId, client.getId());

        // 缓存中也记录一份绑定关系
        addSocketId(gameId, userId, client.getId());
    }
}
