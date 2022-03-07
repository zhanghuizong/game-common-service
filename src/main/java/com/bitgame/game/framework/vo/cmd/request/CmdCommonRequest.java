package com.bitgame.game.framework.vo.cmd.request;

import lombok.Data;

@Data
public class CmdCommonRequest {
    /**
     * 请求 CMD
     */
    private String cmd;

    /**
     * 游戏ID
     */
    private Integer gameId;
}
