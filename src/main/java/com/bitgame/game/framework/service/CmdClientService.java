package com.bitgame.game.framework.service;

import cn.hutool.extra.spring.SpringUtil;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bitgame.game.framework.Client;
import com.bitgame.game.framework.ClientManager;
import com.bitgame.game.framework.ClientManagerMap;
import com.bitgame.game.framework.constant.SysCode;
import com.bitgame.game.framework.constant.SysRoute;
import com.bitgame.game.framework.model.JwtModel;
import com.bitgame.game.framework.util.WsHandlerUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class CmdClientService extends CmdRequestService {
    private final ClientManagerMap clientManagerMap = SpringUtil.getBean(ClientManagerMap.class);

    /**
     * websocket 客户端连接对象
     */
    private final Client client;

    /**
     * @param client  websocket 连接对象
     * @param payload 请求原始数据
     */
    public CmdClientService(Client client, String payload) {
        super(client.getCommonId(), payload);

        this.client = client;
        this.initClient();
    }

    /**
     * 初始 Client 实例对象
     */
    private void initClient() {
        String gameId = client.getGameId();
        ClientManager clientManager = clientManagerMap.get(gameId);

        try {
            // 该客户端已初始化
            JwtModel jwtModel = client.getJwt();
            if (jwtModel != null && !"".equals(jwtModel.getUserId())) {
                return;
            }

            // 初始化客户端实例对象
            JwtModel initJwt = getJwt();
            if (initJwt == null) {
                return;
            }

            String userId = "";
            if (!"".equals(initJwt.getUserId())) {
                userId = initJwt.getUserId();
            } else {
                userId = getParams().getString("userId");
            }

            // 用户首次连接
            if (userId != null && !"".equals(userId)) {
                log.debug("用户首次登陆. userId:{}", userId);

                // 反射修改私有变量
                Class<? extends Client> aClass = client.getClass();
                Field userIdField = aClass.getDeclaredField("userId");
                Field openIdField = aClass.getDeclaredField("openId");
                Field showNameField = aClass.getDeclaredField("showName");
                Field jwtField = aClass.getDeclaredField("jwt");
                userIdField.setAccessible(true);
                openIdField.setAccessible(true);
                showNameField.setAccessible(true);
                jwtField.setAccessible(true);

                userIdField.set(client, userId);
                openIdField.set(client, initJwt.getOpenId());
                showNameField.set(client, initJwt.getShowName());
                jwtField.set(client, initJwt);

                // 通知上线
                WsHandlerUtil.callRoute(client, SysRoute.ONLINE.getCode());

                // 检测是否异地登陆
                WsHandlerUtil.checkSingleLogin(clientManager, client);
            }
        } catch (TokenExpiredException e) {
            log.warn("JWT Token 已过期");
            client.fail(SysCode.tokenExpiredCode);
            clientManager.closeClient(client.getId());
        } catch (JWTDecodeException | InvalidClaimException e) {
            log.warn("JWT Token 解密异常");
            client.fail(SysCode.badJwtTokenCode);
            clientManager.closeClient(client.getId());
        } catch (Exception e) {
            log.error("初始化 websocket 客户端异常. ", e);
        }
    }
}
