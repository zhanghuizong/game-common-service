package com.bitgame.game.framework;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 是否开启游戏功能
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GameMarkConfiguration.class)
public @interface EnableGame {
}
