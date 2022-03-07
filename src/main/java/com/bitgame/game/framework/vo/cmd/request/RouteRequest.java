package com.bitgame.game.framework.vo.cmd.request;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class RouteRequest {
    /**
     * 请求CMD
     */
    private String cmd;

    /**
     * 目标执行控制器实例对象
     */
    private Object obj;

    /**
     * 目标执行控制器方法体对象
     */
    private Method method;
}
