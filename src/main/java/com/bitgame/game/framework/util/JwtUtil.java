package com.bitgame.game.framework.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bitgame.game.framework.config.game.JwtConfig;
import com.bitgame.game.framework.model.JwtModel;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtUtil {
    private static final JwtConfig jwtConfig = BeanUtil.getBean(JwtConfig.class);

    /**
     * 创建 JWT 密文数据
     *
     * @param data 加密数据
     * @return String
     */
    public static String encode(Map<String, Object> data) {
        return encode(data, jwtConfig.getSecret());
    }

    /**
     * 创建 JWT 密文数据
     *
     * @param data 加密数据
     * @return String
     */
    public static String encode(JwtModel data) {
        return encode(JSON.parseObject(JSON.toJSON(data).toString()), jwtConfig.getSecret());
    }

    /**
     * 创建 JWT 密文数据
     *
     * @param data   加密数据
     * @param secret 秘钥
     * @return String
     */
    public static String encode(Map<String, Object> data, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTCreator.Builder builder = JWT.create();

            // 设置 token 签发时间
            builder.withIssuedAt(new Date());

            // 设置 token 过期时间
            builder.withExpiresAt(DateUtil.offsetSecond(new Date(), jwtConfig.getExpireSecondIncrement()));

            builder.withClaim("data", data);

            return builder.sign(algorithm);
        } catch (Exception e) {
            log.error("jwt encode 异常. data:{}, secret:{}", JSON.toJSON(data), secret, e);
        }

        return "";
    }

    /**
     * 解密JWT
     *
     * @param token  String
     * @param secret String
     * @return Map
     */
    public static Map<String, Claim> decode(String token, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getClaims();
    }
}
