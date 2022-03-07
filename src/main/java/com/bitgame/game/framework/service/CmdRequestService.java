package com.bitgame.game.framework.service;

import com.alibaba.fastjson.JSONObject;
import com.bitgame.game.framework.config.game.GameConfig;
import com.bitgame.game.framework.model.JwtModel;
import com.bitgame.game.framework.util.BeanUtil;
import com.bitgame.game.framework.util.WsHandlerUtil;
import com.bitgame.game.framework.vo.cmd.request.CmdRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CmdRequestService {
    /**
     * 请求 CMD
     */
    private String cmd;

    /**
     * 请求参数
     */
    private JSONObject params;

    /**
     * JWT 密文数据原始数据
     */
    private String jwtOrigin;

    /**
     * @param commonId 用作于对请求数据解密
     * @param payload  请求原始数据
     */
    public CmdRequestService(String commonId, String payload) {
        if (!(payload != null && !"".equals(payload))) {
            return;
        }

        // 解析请求报文
        CmdRequest cmdRequest = WsHandlerUtil.getParseOriginData(commonId, payload);
        if (cmdRequest == null) {
            return;
        }

        this.cmd = cmdRequest.getCmd();
        this.params = cmdRequest.getParams();
        if (this.params != null) {
            this.jwtOrigin = params.getString("jwt");
            this.params.remove("jwt");
        }
    }


    /**
     * 获取请求 CMD
     *
     * @return String
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * 获取请求参数
     *
     * @return JSONObject
     */
    public JSONObject getParams() {
        if (params != null) {
            params.put("cmd", getCmd());
        }

        return params;
    }

    /**
     * JWT 用户数据模型
     *
     * @return UserJwtModel
     */
    public JwtModel getJwt() {
        if (jwtOrigin == null) {
            return null;
        }

        GameConfig gameConfig = BeanUtil.getBean(GameConfig.class);
        String secret = gameConfig.getJwt().getSecret();

        return WsHandlerUtil.getJwtData(jwtOrigin, secret);
    }
}
