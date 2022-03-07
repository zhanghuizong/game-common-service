package com.bitgame.game.framework;

import com.bitgame.game.framework.vo.cmd.request.RouteRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RouteMap extends ConcurrentHashMap<String, RouteRequest> {

    public void addRoute(RouteRequest routeRequest) {
        if (routeRequest == null) {
            return;
        }

        put(routeRequest.getCmd(), routeRequest);
    }
}
