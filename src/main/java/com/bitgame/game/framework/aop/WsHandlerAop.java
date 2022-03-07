package com.bitgame.game.framework.aop;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import com.bitgame.game.framework.Client;
import com.bitgame.game.framework.ClientManager;
import com.bitgame.game.framework.ClientManagerMap;
import com.bitgame.game.framework.config.game.GameConfig;
import com.bitgame.game.framework.constant.Protocol;
import com.bitgame.game.framework.model.JwtModel;
import com.bitgame.game.framework.service.CmdRequestService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

/**
 * 对 com.bitgame.game.ws.handler.WsHandler 类增加 AOP 功能.
 * <p>
 * 实现功能:<br>
 * 1. 为请求日志增加 traceId 字段
 * </p>
 */
@Aspect
@Slf4j
@Component
public class WsHandlerAop {
    @Resource
    private ClientManagerMap clientManagerMap;

    @Resource
    private GameConfig gameConfig;

    @Pointcut("execution(public * com.bitgame.game.framework.handler.WsHandler.*(..))")
    public void WsHandler() {

    }

    @Before("WsHandler()")
    public void doBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        if ("supportsPartialMessages".equals(methodName)) {
            return;
        }

        // 目标方法:请求参数
        Object[] args = joinPoint.getArgs();
        WebSocketSession session = (WebSocketSession) args[0];

        String gameId = Convert.toStr(session.getAttributes().get("gameId"), "");
        String userId = getUserId(session, args);

        if (gameId != null && !"".equals(gameId)) {
            MDC.put("game-id", gameId);
        }

        if (userId != null && !"".equals(userId)) {
            MDC.put("user-id", userId);
        }

        if (MDC.get("X-B3-TraceId") == null) {
            MDC.put("X-B3-TraceId", IdUtil.objectId());
        }
    }

    @After(value = "WsHandler()")
    public void doAfterAdvice(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        if ("supportsPartialMessages".equals(name)) {
            return;
        }

        MDC.clear();
    }

    private String getUserId(WebSocketSession session, Object[] args) {
        ClientManager clientManager = clientManagerMap.get(session);
        if (clientManager == null) {
            return null;
        }

        Client client = clientManager.getClient(session.getId());
        if (client != null && client.getUserId() != null) {
            return client.getUserId();
        }

        try {
            // PING PONG 命令不解析
            WebSocketMessage<?> message = (WebSocketMessage<?>) args[1];
            String msg = message.getPayload().toString();
            if (Protocol.PING.getId().equals(msg)) {
                return null;
            }

            String commonId = Convert.toStr(session.getAttributes().get("commonId"), "");
            CmdRequestService cmdRequest = new CmdRequestService(commonId, msg);
            JwtModel jwtData = cmdRequest.getJwt();

            return jwtData != null ? jwtData.getUserId() : null;
        } catch (Throwable ignored) {
        }

        return null;
    }
}
