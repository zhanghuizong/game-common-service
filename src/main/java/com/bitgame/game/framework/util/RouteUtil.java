package com.bitgame.game.framework.util;

import com.alibaba.fastjson.JSON;
import com.bitgame.game.framework.Client;
import com.bitgame.game.framework.ICode;
import com.bitgame.game.framework.constant.SysCode;
import com.bitgame.game.framework.vo.cmd.request.RouteRequest;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.util.Set;

@Slf4j
public class RouteUtil {
    /**
     * 路由调用
     *
     * @param router 类对象
     * @param client websocket 客户端
     * @param cmd    请求 cmd
     * @param params 请求参数
     */
    public static void callRoute(RouteRequest router, Client client, String cmd, String params) {
        Method method = router.getMethod();
        if (method == null) {
            log.warn("请求 CMD 不存在. cmd:{}, params:{}", cmd, params);
            client.fail(SysCode.noCmdCode);
            return;
        }

        // 方法参数个数
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 2) {
            log.error("被调用目标方法参数太多. cmd:{}, params:{}", cmd, params);
            return;
        }

        // 被调用方法支持两个参数
        // 1. 第一个参数: 必须是 Client 对象
        // 2. String || Request 请求实例对象
        try {
            Object request = params;

            // 第一个参数
            String firstType = parameterTypes[0].getName();
            if (!Client.class.getName().equals(firstType)) {
                log.error("被调用目标方法. 第一个参数类型有误. cmd:{}, params:{}", cmd, params);
                return;
            }

            // 第二个参数类型. String 类型参数不做解析
            String secondType = parameterTypes[1].getName();
            if (!String.class.getName().equals(secondType)) {
                request = JSON.parseObject(params, parameterTypes[1]);

                // 参数验证器
                Validator validator = BeanUtil.getBean(Validator.class);
                Set<? extends ConstraintViolation<?>> validate = validator.validate(request);
                if (validate.size() > 0) {
                    client.push(cmd, ICode.get(SysCode.requestParamsMiss), validate.iterator().next().getMessage());
                    return;
                }
            }

            Long sTime = System.currentTimeMillis();
            method.invoke(router.getObj(), client, request);
            Long eTime = System.currentTimeMillis();
            log.info("CMD执行耗时. cmd:{}, time:{}", cmd, eTime - sTime);
        } catch (Exception e) {
            log.error("系统异常. cmd:{}, params:{}", cmd, params, e);
            client.fail(SysCode.requestExecuteException, e.getMessage());
        }
    }
}
