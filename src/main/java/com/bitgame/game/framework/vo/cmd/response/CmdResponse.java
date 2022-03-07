package com.bitgame.game.framework.vo.cmd.response;

import com.bitgame.game.framework.ICode;
import com.bitgame.game.framework.constant.SysCode;
import lombok.Data;

/**
 * CMD 请求返回报文
 *
 * @param <T>
 */
@Data
public class CmdResponse<T> {
    private Integer code;
    private String cmd;
    private String msg;
    private T data;

    public CmdResponse(String cmd, T data) {
        this(cmd, SysCode.OK.getCode(), SysCode.OK.getMsg(), data);
    }

    public CmdResponse(String cmd, Integer code, String msg) {
        this(cmd, code, msg, null);
    }

    public CmdResponse(String cmd, Integer code, String msg, T data) {
        this.code = code;
        this.cmd = cmd;
        this.msg = SysCode.OK.getCode().equals(code) ? null : msg;
        this.data = data;
    }

    /**
     * @param cmd  响应CMD
     * @param data 响应数据
     * @return CmdResponse
     */
    public static <T> CmdResponse<T> ok(String cmd, T data) {
        return ok(cmd, ICode.get(SysCode.OK), data);
    }

    /**
     * @param cmd  响应CMD
     * @param code 响应码
     * @return CmdResponse
     */
    public static <T> CmdResponse<T> ok(String cmd, ICode code) {
        return ok(cmd, code, null);
    }

    /**
     * @param cmd  响应CMD
     * @param code 响应码
     * @param data 响应数据
     * @return CmdResponse
     */
    public static <T> CmdResponse<T> ok(String cmd, ICode code, T data) {
        return new CmdResponse<T>(cmd, code.getCode(), code.getMsg(), data);
    }
}
