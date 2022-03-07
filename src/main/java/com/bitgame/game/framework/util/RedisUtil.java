package com.bitgame.game.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
public final class RedisUtil {
    @SuppressWarnings("unchecked")
    private final static RedisTemplate<String, Object> redisTemplate = BeanUtil.getBean("redisTemplate", RedisTemplate.class);

    public static Boolean setNx(String gameId, String key, Object value, long timeout) {
        String fKey = BaseUtil.getKey(gameId, key);
        log.debug("RedisUtil.setNx key:{}", fKey);

        return redisTemplate.opsForValue().setIfAbsent(fKey, value, timeout, TimeUnit.SECONDS);
    }

    public static void hSet(String gameId, String key, String field, Object value) {
        String fKey = BaseUtil.getKey(gameId, key);
        log.debug("RedisUtil.hSet. key:{}", fKey);

        redisTemplate.opsForHash().put(fKey, field, value);
    }

    public static Long hDel(String gameId, String key, String field) {
        String fKey = BaseUtil.getKey(gameId, key);
        log.debug("RedisUtil.hDel. key:{}", fKey);

        return redisTemplate.opsForHash().delete(fKey, field);
    }

    public static Object hGet(String gameId, String key, String field) {
        String fKey = BaseUtil.getKey(gameId, key);
        log.debug("RedisUtil.hGet. key:{}", fKey);

        return redisTemplate.opsForHash().get(fKey, field);
    }

    /**
     * 发布消息
     *
     * @param gameId  游戏ID
     * @param key     频道
     * @param message 消息内容
     */
    public static void publish(String gameId, String key, Object message) {
        String fKey = BaseUtil.getKey(gameId, key);
        log.debug("RedisUtil.publish. key:{}", fKey);

        redisTemplate.convertAndSend(fKey, message);
    }
}
