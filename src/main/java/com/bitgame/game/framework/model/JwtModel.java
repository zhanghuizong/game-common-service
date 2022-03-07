package com.bitgame.game.framework.model;

import lombok.Data;
import lombok.ToString;

/**
 * JWT data 数据模型
 * <pre>
 * {
 *      "data": {
 *          "gameId": 10009,
 *          "userId": 946319020,
 *          "email": "zhan***@163.com",
 *          "showName": "zhan***@163.com",
 *          "openId": "29A19E5D9E9848AB9AB8C5923EFFE36C",
 *          "payPwdFlag": false,
 *          "pwdFlag": true,
 *          "uid": "29A19E5D9E9848AB9AB8C5923EFFE36C",
 *          "domain": "testbitgame.com",
 *          "timestamp": 1605678337945
 *      },
 *      "iat": 1605678337,
 *      "exp": 1670382943
 * }
 * </pre>
 */
@Data
@ToString
public class JwtModel {
    private String gameId;

    private String userId;

    private String email;

    private String showName;

    private String openId;

    private Boolean payPwdFlag;

    private Boolean pwdFlag;

    private String uid;

    private String domain;

    private String channel;

    private Long timestamp;
}

// "gameId" -> {Integer@20193} 10009
// "uid" -> "54F88E24FB3543A68A0C1EEBE8E23DE7"
// "payPwdFlag" -> {Boolean@20213} true
// "showName" -> "1***@qq.com"
// "pwdFlag" -> {Boolean@20213} true
// "openId" -> "54F88E24FB3543A68A0C1EEBE8E23DE7"
// "domain" -> "testbitgame.com"
// "channel" -> "BITGAME"
// "userId" -> {Integer@20272} 100000054
// "email" -> "1***@qq.com"
// "timestamp" -> {Long@20292} 1637552791672
