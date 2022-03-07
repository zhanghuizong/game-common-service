package com.bitgame.game.framework;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.bitgame.game.framework.config.game.GameConfig;
import com.bitgame.game.framework.constant.Encoder;
import com.bitgame.game.framework.constant.Protocol;
import com.bitgame.game.framework.constant.SysCode;
import com.bitgame.game.framework.model.JwtModel;
import com.bitgame.game.framework.util.BeanUtil;
import com.bitgame.game.framework.util.PushUtil;
import com.bitgame.game.framework.vo.cmd.response.CmdResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * websocket 连接客户端
 */
@Slf4j
public final class Client {
    /**
     * socketId
     */
    @Getter
    private final String id;

    /**
     * 秘钥ID
     */
    @Getter
    private final String commonId;

    /**
     * ws 连接会话
     */
    @Getter
    private final WebSocketSession session;

    /**
     * 游戏ID (禁止修改)
     */
    @Getter
    private final String gameId;

    /**
     * 用户ID (禁止修改)
     */
    @Getter
    private String userId;

    /**
     * 平台用户ID (禁止修改)
     */
    @Getter
    private String openId;

    /**
     * 用户昵称
     */
    @Getter
    private String showName;

    /**
     * 用户认证信息 (禁止修改)
     */
    @Getter
    private JwtModel jwt;

    /**
     * 游戏配置类
     */
    private final GameConfig gameConfig = BeanUtil.getBean(GameConfig.class);

    public Client(WebSocketSession session) {
        this.id = session.getId();
        this.commonId = Convert.toStr(session.getAttributes().get("commonId"), "");
        this.gameId = Convert.toStr(session.getAttributes().get("gameId"), gameConfig.getId());
        this.session = session;
    }

    /**
     * 正确消息推送
     *
     * @param cmd 请求返回CMD
     */
    public void push(String cmd) {
        push(cmd, null);
    }

    /**
     * 正确消息推送
     *
     * @param cmd     请求返回CMD
     * @param message 请求返回数据
     */
    public void push(String cmd, Object message) {
        push(cmd, null, message);
    }

    /**
     * 正确消息推送
     *
     * @param cmd  请求返回CMD
     * @param code 响应码
     */
    public void push(String cmd, ICode code) {
        push(cmd, code, null);
    }

    /**
     * 正确消息推送
     *
     * @param cmd     请求返回CMD
     * @param code    响应码
     * @param message 请求返回数据
     */
    public void push(String cmd, ICode code, Object message) {
        pushOrigin(PushUtil.getCmdResponse(cmd, code, message));
    }

    /**
     * 系统框架底层内部错误
     *
     * @param code 错误码
     */
    public void fail(SysCode code) {
        fail(code, null);
    }

    /**
     * 系统框架底层内部错误
     *
     * @param code 错误码
     * @param data 响应数据
     */
    public void fail(SysCode code, String data) {
        push("conn::error", ICode.get(code), data);
    }

    /**
     * 应用程序错误
     *
     * @param code 错误码
     */
    public void pushError(ICode code) {
        pushError(code, null);
    }

    /**
     * 应用程序错误
     *
     * @param code 错误码
     * @param data 响应数据
     */
    public void pushError(ICode code, Object data) {
        push("error", code, data);
    }

    /**
     * 不做数据格式封装直接推送至客户端
     *
     * @param message 数据模型
     */
    public synchronized void pushOrigin(Object message) {
        if (message == null) {
            return;
        }

        try {
            String content = message instanceof CmdResponse ? JSON.toJSON(message).toString() : message.toString();
            if (gameConfig.getIsShowPushLog()) {
                log.info("消息推送:{}", content);
            }

            // 判断 websocket 连接通道是否关闭
            if (!getSession().isOpen()) {
                log.error("websocket 连接实例对象已经关闭. message:{}", content);
                return;
            }

            // 是否启用数据加密传输
            String pushContent = content;
            if (gameConfig.getAuth()) {
                AES aes = SecureUtil.aes(StrUtil.bytes(getCommonId()));
                pushContent = Protocol.BUSINESS.getId() + aes.encryptBase64(content);
            }

            // 使用 json 数据格式返回
            if (gameConfig.getEncoder().equals(Encoder.TEXT.getId())) {
                if (getSession().isOpen()) getSession().sendMessage(new TextMessage(pushContent));
                return;
            }

            // 二进制推送
            if (getSession().isOpen()) getSession().sendMessage(new BinaryMessage(pushContent.getBytes()));
        } catch (Exception e) {
            log.error("发送消息异常. err:", e);
        }
    }

    /**
     * 关闭 websocket 连接通道
     */
    public void close() {
        try {
            session.close();
        } catch (Exception e) {
            log.error("主动关闭 websocket 通道异常. err:", e);
        }
    }
}
