package com.bitgame.game.framework.config.redis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * 自定义一个 Redis key 序列化规则
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class DefaultKeySerializer extends StringRedisSerializer {
    private final ConfigurableEnvironment environment;

    private String prefix;
    private String appName;
    private String gameId;

    public DefaultKeySerializer(ConfigurableEnvironment environment) {
        this.environment = environment;
        this.appName = environment.getProperty("spring.application.name");
        this.gameId = environment.getProperty("game.id");
        this.prefix = String.format("%s:%s:", appName, gameId);
    }

    @Override
    public String deserialize(byte[] bytes) {
        return super.deserialize(bytes);
    }

    @Override
    public byte[] serialize(String string) {
        return super.serialize(prefix + string);
    }
}
