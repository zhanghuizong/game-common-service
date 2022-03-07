package com.bitgame.game.framework.vo.cmd.request;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 请求参数数据格式
 */
@Data
public class CmdRequest {
    String cmd;

    JSONObject params;
}
