package com.bitgame.game.framework;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameMarkConfiguration {
    @Bean
    public Marker getGameMarkConfiguration() {
        return new Marker();
    }

    public static class Marker {

    }
}
