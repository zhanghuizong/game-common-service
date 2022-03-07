package com.bitgame.game.framework.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.bitgame.game.framework.Client;
import com.bitgame.game.framework.ClientManager;
import com.bitgame.game.framework.RouteMap;
import com.bitgame.game.framework.config.game.GameConfig;
import com.bitgame.game.framework.dao.LoginDao;
import com.bitgame.game.framework.model.JwtModel;
import com.bitgame.game.framework.vo.cmd.request.CmdRequest;
import com.bitgame.game.framework.vo.cmd.request.RouteRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * websocket 常用业务逻辑处理工具类
 */
@Slf4j
public class WsHandlerUtil {
    private final static RouteMap routeMap = BeanUtil.getBean(RouteMap.class);

    /**
     * 解密请求数据
     *
     * @param commonId   秘钥ID
     * @param originData 加密原始数据
     * @return JSONObject
     */
    public static CmdRequest getParseOriginData(String commonId, String originData) {
        try {
            Boolean auth = BeanUtil.getBean(GameConfig.class).getAuth();
            String requestData = originData;

            // 解密
            if (auth) {
                String parseText = originData.substring(1);
                AES aes = SecureUtil.aes(StrUtil.bytes(commonId));
                byte[] decrypt = aes.decrypt(parseText);
                requestData = StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
            }

            return JSONObject.parseObject(requestData, CmdRequest.class);
        } catch (Exception e) {
            log.error("解密及转换请求数据异常. commonId:{}, originData:{}", commonId, originData, e);
        }

        return null;
    }

    /**
     * 获取 JWT 数据对象
     *
     * @param encodeJwt 加密 JWT 数据
     * @param secret    JWT 秘钥
     * @return JwtDataModel
     */
    public static JwtModel getJwtData(String encodeJwt, String secret) {
        if ("".equals(encodeJwt)) {
            return null;
        }

        Map<String, Claim> jwtJson = JwtUtil.decode(encodeJwt, secret);
        if (jwtJson == null || jwtJson.size() <= 0) {
            log.error("解析JWT异常. encodeJwt:{}, secret:{}", encodeJwt, secret);
            return null;
        }

        return jwtJson.get("data").as(JwtModel.class);
    }

    /**
     * 检测是否存在异地登陆
     *
     * @param clientManager websocket 管理器
     * @param client        websocket 连接对象
     */
    public static void checkSingleLogin(ClientManager clientManager, Client client) {
        LoginDao loginDao = BeanUtil.getBean(LoginDao.class);

        // 判断是否已经登陆过
        loginDao.checkAlreadyLogin(clientManager, client);

        // 绑定关系
        loginDao.bingSocketId(clientManager, client);
    }

    /**
     * websocket 路由分发
     *
     * @param client Client
     * @param cmd    String
     */
    public static void callRoute(Client client, String cmd) {
        callRoute(client, cmd, "");
    }

    /**
     * websocket 路由分发
     *
     * @param client Client
     * @param cmd    请求命令
     * @param params 请求参数
     */
    public static void callRoute(Client client, String cmd, String params) {
        RouteRequest router = routeMap.get(cmd);
        if (router == null) {
            return;
        }

        RouteUtil.callRoute(router, client, cmd, params);
    }
}
