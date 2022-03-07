package com.bitgame.game.framework.util;

import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.lang.reflect.Method;
import java.math.BigDecimal;

@Slf4j
public class BaseUtil {
    /**
     * BigDecimal 去除尾数0
     * 注意: 整数直接 `stripTrailingZeros` 方法会转换成 科学计数法
     *
     * @param num BigDecimal
     * @return String
     */
    public static String toPlainString(BigDecimal num) {
        if (num == null) {
            return "0";
        }

        return num.stripTrailingZeros().toPlainString();
    }

    /**
     * 生产唯一订单号
     *
     * @return string
     */
    public static String getOrderId() {
        String time = String.valueOf(System.currentTimeMillis());
        String md5 = SecureUtil.sha1(RandomStringUtils.randomNumeric(22));
        String substring = md5.substring(0, 8);

        return time + "A" + substring;
    }

    public static Jackson2JsonRedisSerializer<Object> getJackson() {
        Jackson2JsonRedisSerializer<Object> jackson = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson.setObjectMapper(om);

        return jackson;
    }

    /**
     * 获取 class 对象方法实例
     *
     * @param aClass   类对象
     * @param findName 方法名称
     * @return Method
     */
    public static Method getMethod(Class<?> aClass, String findName) {
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (name.equals(findName)) {
                return method;
            }
        }

        return null;
    }

    /**
     * 构造 Redis 健值
     *
     * @param gameId 游戏ID
     * @param suffix 后缀
     * @return String
     */
    public static String getKey(String gameId, String suffix) {
        ConfigurableEnvironment environment = BeanUtil.getBean(ConfigurableEnvironment.class);
        String prefix = String.format("%s:%s:", environment.getProperty("spring.application.name"), gameId);

        return String.format("%s%s", prefix, suffix);
    }
}
