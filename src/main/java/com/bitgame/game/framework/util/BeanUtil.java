package com.bitgame.game.framework.util;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BeanUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return Optional.of(context.getBean(clazz)).orElse(null);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return Optional.of(context.getBean(name, clazz)).orElse(null);
    }
}
