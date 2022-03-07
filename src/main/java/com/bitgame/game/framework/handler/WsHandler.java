package com.bitgame.game.framework.handler;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.bitgame.game.framework.Client;
import com.bitgame.game.framework.ClientManager;
import com.bitgame.game.framework.ClientManagerMap;
import com.bitgame.game.framework.config.game.GameConfig;
import com.bitgame.game.framework.constant.Protocol;
import com.bitgame.game.framework.constant.SysCode;
import com.bitgame.game.framework.service.CmdClientService;
import com.bitgame.game.framework.util.WsHandlerUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@Slf4j
public class WsHandler implements WebSocketHandler {
    @Resource
    private ClientManagerMap clientManagerMap;

    @Resource
    private GameConfig gameConfig;

    /**
     * 客户端首次连接时调用
     *
     * @param session WebSocketSession
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("用户上线:{}", session.getId());

        if (gameConfig.getAuth()) {
            String commonId = Convert.toStr(session.getAttributes().get("commonId"), "");
            if (commonId == null || "".equals(commonId)) {
                log.error("会话连接失败. commonId 参数异常. 立即关闭会话连接");
                closeSession(session);
                return;
            }

            log.debug("commonId:{}", commonId);
        }

        try {
            // 沿袭与 nodejs 兼容与客户端使用. 与服务器没有任何实际意义
            // 发送初始化数据. 客户端使用
            session.sendMessage(new TextMessage(Protocol.INIT.getId()));

            clientManagerMap.get(session).addClient(new Client(session));
        } catch (IOException e) {
            log.error("发送初始化数据. 客户端使用异常.", e);
        }
    }

    /**
     * 客户端发送消息时调用
     *
     * @param session WebSocketSession
     * @param message WebSocketMessage
     */
    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) {
        ClientManager clientManager = clientManagerMap.get(session);
        Client client = clientManager.getClient(session.getId());
        if (client == null) {
            log.error("websocket 连接实例对象绑定存在异常");
            clientManager.closeClient(session.getId());
            return;
        }

        // 发送 PING PONG 命令
        String msg = message.getPayload().toString();
        if (Protocol.PING.getId().equals(msg)) {
            try {
                session.sendMessage(new TextMessage(Protocol.PONG.getId()));
            } catch (IOException e) {
                log.error("下发 PING/PONG 异常.", e);
            }

            return;
        }

        try {
            // 请求 CMD 判断
            CmdClientService cmdClient = new CmdClientService(client, msg);
            String cmd = cmdClient.getCmd();
            if ("".equals(cmd) || cmd == null) {
                client.fail(SysCode.noCmdCode);
                return;
            }

            // 请求参数
            JSONObject params = cmdClient.getParams();
            if (gameConfig.getIsShowRequestLog()) {
                log.info("消息接收:{}", JSONObject.toJSONString(params));
            }

            // CMD 路由转发
            WsHandlerUtil.callRoute(client, cmd, params.toJSONString());
        } catch (Exception e) {
            log.error("系统异常.", e);
        }
    }

    /**
     * websocket 消息传输异常
     *
     * @param session   websocket 会话
     * @param exception 异常
     */
    @Override
    public void handleTransportError(WebSocketSession session, @NonNull Throwable exception) {
        log.error("handleTransportError. sessionId:{}", session.getId(), exception);
        clientManagerMap.get(session).removeClient(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        log.debug("afterConnectionClosed. sessionId:{}, closeStatus:{}", session.getId(), closeStatus.toString());
        clientManagerMap.get(session).removeClient(session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void closeSession(WebSocketSession session) {
        try {
            session.close();
        } catch (IOException e) {
            log.error("关闭 websocket 会话异常.", e);
        }
    }
}
