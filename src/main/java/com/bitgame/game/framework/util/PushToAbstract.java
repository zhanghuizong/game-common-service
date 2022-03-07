package com.bitgame.game.framework.util;

import com.alibaba.fastjson.JSON;
import com.bitgame.game.framework.Client;
import com.bitgame.game.framework.ClientManagerMap;
import com.bitgame.game.framework.ICode;
import com.bitgame.game.framework.constant.SysCode;
import com.bitgame.game.framework.constant.SysRoute;
import com.bitgame.game.framework.dao.PublishDao;
import com.bitgame.game.framework.vo.cmd.response.CmdResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 消息推送工具类
 */
@Slf4j
abstract class PushToAbstract {
    private static final ClientManagerMap clientManagerMap = BeanUtil.getBean(ClientManagerMap.class);
    private static final PublishDao publishDao = BeanUtil.getBean(PublishDao.class);

    /**
     * 全服消息推送
     *
     * @param cmd     响应CMD
     * @param message 数据模型
     */
    public static void pushAll(String gameId, String cmd, Object message) {
        pushAll(gameId, cmd, ICode.get(SysCode.OK), message);
    }

    /**
     * 全服消息推送
     *
     * @param cmd  响应CMD
     * @param code 推送错误码
     */
    public static void pushAll(String gameId, String cmd, ICode code) {
        pushAll(gameId, cmd, code, null);
    }

    /**
     * 全服消息推送
     *
     * @param cmd     响应CMD
     * @param code    推送错误码
     * @param message 数据模型
     */
    public static void pushAll(String gameId, String cmd, ICode code, Object message) {
        List<String> users = clientManagerMap.get(gameId).getUsers();
        for (String userId : users) {
            push(gameId, cmd, userId, code, message);
        }

        // 跨服推送
        publish(gameId, null, getCmdResponse(cmd, code, message));
    }

    /**
     * 单用户消息推送
     *
     * @param cmd     响应CMD
     * @param userId  消息推送用户ID
     * @param message 数据模型
     */
    public static void push(String gameId, String cmd, String userId, Object message) {
        push(gameId, cmd, userId, ICode.get(SysCode.OK), message);
    }

    /**
     * 单用户消息推送
     *
     * @param cmd    响应CMD
     * @param userId 消息推送用户ID
     * @param code   响应码
     */
    public static void push(String gameId, String cmd, String userId, ICode code) {
        push(gameId, cmd, userId, code, null);
    }

    /**
     * 批量消息推送
     *
     * @param cmd     响应CMD
     * @param userId  消息推送用户ID
     * @param code    响应码
     * @param message 响应内容
     */
    public static void push(String gameId, String cmd, String userId, ICode code, Object message) {
        send(gameId, userId, cmd, code, message);
    }

    /**
     * 批量消息推送
     *
     * @param users   消息推送用户列表
     * @param message 数据模型
     */
    public static void push(String gameId, String cmd, List<String> users, Object message) {
        users.forEach((userId) -> {
            send(gameId, userId, cmd, ICode.get(SysCode.OK), message);
        });
    }

    /**
     * 批量消息推送
     *
     * @param cmd   响应CMD
     * @param users 用户列表
     * @param code  响应码
     */
    public static void push(String gameId, String cmd, List<String> users, ICode code) {
        users.forEach((userId) -> {
            send(gameId, userId, cmd, code, null);
        });
    }

    /**
     * 批量消息推送
     *
     * @param cmd     响应CMD
     * @param users   用户列表
     * @param code    响应码
     * @param message 响应内容
     */
    public static void push(String gameId, String cmd, List<String> users, ICode code, Object message) {
        users.forEach((userId) -> {
            send(gameId, userId, cmd, code, message);
        });
    }

    /**
     * 获取 CMD 统一返回数据格式
     *
     * @param cmd     请求CMD
     * @param code    响应码
     * @param message 响应数据
     * @return CmdResponse
     */
    public static CmdResponse<Object> getCmdResponse(String cmd, ICode code, Object message) {
        if (code == null) {
            code = ICode.get(SysCode.OK);
        }

        return CmdResponse.ok(cmd, code, message);
    }

    /**
     * 执行消息推送逻辑
     *
     * @param userId  用户ID
     * @param cmd     响应CMD
     * @param code    响应码
     * @param message 响应内容
     */
    static void send(String gameId, String userId, String cmd, ICode code, Object message) {
        if (userId == null || "".equals(userId)) {
            log.warn("执行消息推送逻辑. userId 为空");
            return;
        }

        log.debug("执行消息推送逻辑. userId:{}, cmd:{}", userId, cmd);
        CmdResponse<Object> cmdResponse = getCmdResponse(cmd, code, message);
        Client client = clientManagerMap.get(gameId).getClientByUserId(userId);
        if (client != null) {
            client.pushOrigin(cmdResponse);
            return;
        }

        log.debug("执行消息推送逻辑:需跨服推送. userId:{}, cmd:{}", userId, cmd);
        publish(gameId, userId, cmdResponse);
    }

    /**
     * 消息跨服推送
     *
     * @param userId      用户ID
     * @param cmdResponse 响应内容
     */
    static void publish(String gameId, String userId, CmdResponse<Object> cmdResponse) {
        String[] users = (userId == null || "".equals(userId)) ? new String[]{} : new String[]{userId};

        publishDao.publish(
                gameId,
                users,
                SysRoute.PUBLISH.getCode(),
                JSON.parse(JSON.toJSON(cmdResponse).toString())
        );
    }
}
