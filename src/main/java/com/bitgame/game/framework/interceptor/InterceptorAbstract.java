package com.bitgame.game.framework.interceptor;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.bitgame.game.framework.config.game.GameConfig;
import com.bitgame.game.framework.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public abstract class InterceptorAbstract extends HttpSessionHandshakeInterceptor {
    private final GameConfig gameConfig;

    public InterceptorAbstract(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    /**
     * 检测 websocket 协议安全认证
     *
     * @param servletRequest 请求参数
     */
    public String getCommonId(HttpServletRequest servletRequest) {
        if (!gameConfig.getAuth()) {
            return "";
        }

        // websocket 协议安全认证
        String auth = servletRequest.getParameter("auth");
        if ("".equals(auth) || auth == null) {
            throw new BusinessException("websocket 连接参数 auth 为空");
        }

        // SpringBoot 会自动将 + 符号 变成 空格符号
        auth = auth.replaceAll("\\s", "+");

        String privateKey = gameConfig.getRsa().getPrivateKey();
        RSA rsa = new RSA(privateKey, null);
        byte[] tests = rsa.decrypt(auth, KeyType.PrivateKey);
        String commonId = StrUtil.str(tests, CharsetUtil.CHARSET_UTF_8);
        if ("".equals(commonId) || commonId == null) {
            throw new BusinessException("commonId 解密异常");
        }

        return commonId;
    }

    /**
     * websocket 上下文注入 gameId 参数
     *
     * @param servletRequest 请求参数
     */
    public String setGameId(HttpServletRequest servletRequest) {
        String gameId = servletRequest.getParameter("gameId");
        if (gameId == null || "".equals(gameId)) {
            gameId = gameConfig.getId();
        }

        if (gameId == null || "".equals(gameId)) {
            throw new BusinessException("gameId 参数不能为空");
        }

        return gameId;
    }
}
