package com.bitgame.game.framework.util;

import com.bitgame.game.framework.ICode;
import com.bitgame.game.framework.config.game.GameConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 消息推送工具类
 */
@Slf4j
public final class PushUtil extends PushToAbstract {
    private static final GameConfig gameConfig = BeanUtil.getBean(GameConfig.class);

    /**
     * 全服消息推送
     *
     * @param cmd     响应CMD
     * @param message 数据模型
     */
    public static void pushAll(String cmd, Object message) {
        pushAll(gameConfig.getId(), cmd, message);
    }

    /**
     * 全服消息推送
     *
     * @param cmd  响应CMD
     * @param code 推送错误码
     */
    public static void pushAll(String cmd, ICode code) {
        pushAll(gameConfig.getId(), cmd, code);
    }

    /**
     * 全服消息推送
     *
     * @param cmd     响应CMD
     * @param code    推送错误码
     * @param message 数据模型
     */
    public static void pushAll(String cmd, ICode code, Object message) {
        pushAll(gameConfig.getId(), cmd, code, message);
    }

    /**
     * 单用户消息推送
     *
     * @param cmd     响应CMD
     * @param userId  消息推送用户ID
     * @param message 数据模型
     */
    public static void push(String cmd, String userId, Object message) {
        push(gameConfig.getId(), cmd, userId, message);
    }

    /**
     * 单用户消息推送
     *
     * @param cmd    响应CMD
     * @param userId 消息推送用户ID
     * @param code   响应码
     */
    public static void push(String cmd, String userId, ICode code) {
        push(gameConfig.getId(), cmd, userId, code);
    }

    /**
     * 批量消息推送
     *
     * @param cmd     响应CMD
     * @param userId  消息推送用户ID
     * @param code    响应码
     * @param message 响应内容
     */
    public static void push(String cmd, String userId, ICode code, Object message) {
        push(gameConfig.getId(), cmd, userId, code, message);
    }

    /**
     * 批量消息推送
     *
     * @param users   消息推送用户列表
     * @param message 数据模型
     */
    public static void push(String cmd, List<String> users, Object message) {
        push(gameConfig.getId(), cmd, users, message);
    }

    /**
     * 批量消息推送
     *
     * @param cmd   响应CMD
     * @param users 用户列表
     * @param code  响应码
     */
    public static void push(String cmd, List<String> users, ICode code) {
        push(gameConfig.getId(), cmd, users, code);
    }

    /**
     * 批量消息推送
     *
     * @param cmd     响应CMD
     * @param users   用户列表
     * @param code    响应码
     * @param message 响应内容
     */
    public static void push(String cmd, List<String> users, ICode code, Object message) {
        push(gameConfig.getId(), cmd, users, code, message);
    }
}
