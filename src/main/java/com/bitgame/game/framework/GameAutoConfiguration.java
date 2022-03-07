package com.bitgame.game.framework;

import cn.hutool.core.util.ClassUtil;
import com.bitgame.game.framework.config.game.GameConfig;
import com.bitgame.game.framework.vo.cmd.request.RouteRequest;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * 自动配置类
 */
@Configuration
@ConditionalOnBean(GameMarkConfiguration.Marker.class)
@EnableWebSocket
public class GameAutoConfiguration implements CommandLineRunner {
    @Resource
    private DefaultListableBeanFactory defaultListableBeanFactory;

    @Resource
    private GameConfig gameConfig;

    @Resource
    private RouteMap routeMap;

    /**
     * 自动扫描注册 @WsController, @WsRequest 注解
     *
     * @param args 系统参数
     */
    @Override
    public void run(String... args) throws Exception {
        List<String> scanPackage = gameConfig.getScanPackage();
        if (scanPackage == null || scanPackage.size() <= 0) {
            throw new Exception("gameConfig.scanPackage 未配置");
        }

        for (String packageName : scanPackage) {
            if ("".equals(packageName)) {
                throw new Exception("gameConfig.scanPackage 包名称配置有误");
            }

            Set<Class<?>> controller = ClassUtil.scanPackage(packageName);
            for (Class<?> aClass : controller) {
                WsController wsController = aClass.getAnnotation(WsController.class);
                if (wsController == null) {
                    continue;
                }

                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    WsRequest wsRequest = method.getAnnotation(WsRequest.class);
                    if (wsRequest == null) {
                        continue;
                    }

                    // 请求CMD为空不做处理
                    String cmdName = wsRequest.value();
                    if ("".equals(cmdName) || cmdName == null) {
                        continue;
                    }

                    // 请求CMD出现重名
                    if (routeMap.get(cmdName) != null) {
                        throw new Exception("请求CMD出现重名. cmdName:" + cmdName);
                    }

                    RouteRequest routeRequest = new RouteRequest();
                    routeRequest.setCmd(cmdName);
                    routeRequest.setMethod(method);
                    routeRequest.setObj(defaultListableBeanFactory.getBean(aClass));
                    routeMap.addRoute(routeRequest);
                }
            }
        }
    }
}
