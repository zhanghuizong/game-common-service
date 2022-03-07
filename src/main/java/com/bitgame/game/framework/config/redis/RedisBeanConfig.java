package com.bitgame.game.framework.config.redis;

import com.bitgame.game.framework.config.redis.listener.RedisMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * 注册 Redis 消息订阅功能
 */
@Configuration
public class RedisBeanConfig {
    /**
     * 配置消息监听器
     *
     * @return RedisMessageListener
     */
    @Bean
    public RedisMessageListener listener() {
        return new RedisMessageListener();
    }

    /**
     * 将消息监听器绑定到消息容器
     *
     * @param redisConnectionFactory Redis实例对象
     * @return RedisMessageListenerContainer
     */
    @Bean
    public RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }
}
