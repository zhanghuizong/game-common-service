package com.bitgame.game.framework.config.apollo;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by caochangshan on 2020/3/18.
 */
public class RefresherConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @ApolloConfigChangeListener({ApolloConfigConsts.COMMON_NAMESPACE, ApolloConfigConsts.GAME_NAMESPACE, ApolloConfigConsts.DEFAULT_NAMESPACE})
    public void onChange(ConfigChangeEvent changeEvent) {
        refreshGatewayProperties(changeEvent);
    }

    private void refreshGatewayProperties(ConfigChangeEvent changeEvent) {
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
    }
}
