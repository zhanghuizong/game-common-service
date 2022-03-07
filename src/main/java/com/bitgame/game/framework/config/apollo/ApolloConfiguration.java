package com.bitgame.game.framework.config.apollo;

import com.ctrip.framework.apollo.spring.annotation.ApolloConfigRegistrar;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by caochangshan on 2020/3/18.
 */
@Configuration
@ConditionalOnMissingBean(RefresherConfiguration.class)
@ConditionalOnClass(ApolloConfigRegistrar.class)
@EnableApolloConfig({ApolloConfigConsts.COMMON_NAMESPACE, ApolloConfigConsts.GAME_NAMESPACE, ApolloConfigConsts.DEFAULT_NAMESPACE})
public class ApolloConfiguration {

    @Bean
    public RefresherConfiguration refresherConfiguration() {
        return new RefresherConfiguration();
    }
}
