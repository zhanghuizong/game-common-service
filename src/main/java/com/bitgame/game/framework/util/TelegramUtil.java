package com.bitgame.game.framework.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bitgame.game.framework.config.TelegramConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramUtil {
    private static TelegramConfig telegramConfig;
    private static ConfigurableEnvironment environment;
    private static final RestTemplate restTemplate = new RestTemplate();
    private static String localIp;

    public TelegramUtil(TelegramConfig telegramConfig, ConfigurableEnvironment environment) {
        TelegramUtil.telegramConfig = telegramConfig;
        TelegramUtil.environment = environment;
    }

    private static String getLocalIP() {
        if (null != localIp) {
            return localIp;
        }

        try {
            InetAddress ia = InetAddress.getLocalHost();
            localIp = ia.getHostAddress();
        } catch (Exception e) {
            log.warn("获取本机ip失败:", e);
        }

        return localIp;
    }

    /**
     * 发送 telegram 报警
     *
     * @param lockKey key
     * @param msg     发送消息内容
     */
    public static void sendMsg(String gameId, String lockKey, String msg) {
        sendMsg(gameId, lockKey, msg, null, null);
    }

    /**
     * 发送 telegram 报警
     *
     * @param lockKey key
     * @param msg     发送消息内容
     * @param expired 过期时间. 默认 5min
     */
    public static void sendMsg(String gameId, String lockKey, String msg, Long expired) {
        sendMsg(gameId, lockKey, msg, null, expired);
    }

    /**
     * 发送 telegram 报警
     *
     * @param lockKey  key
     * @param msg      发送消息内容
     * @param extField 消息模板扩展字段
     */
    public static void sendMsg(String gameId, String lockKey, String msg, List<String> extField) {
        sendMsg(gameId, lockKey, msg, extField, null);
    }

    /**
     * 发送 telegram 报警
     *
     * @param lockKey  key
     * @param msg      发送消息内容
     * @param extField 消息模板扩展字段
     * @param expired  过期时间. 默认 5min
     */
    public static void sendMsg(String gameId, String lockKey, String msg, List<String> extField, Long expired) {
        if (!telegramConfig.getIsSend()) {
            return;
        }

        if (expired == null) {
            expired = 300L;
        }

        if (!RedisUtil.setNx(gameId, lockKey, 1, expired)) {
            log.warn("发送 telegram 报警频繁. key:{}, msg:{}, expired:{}", lockKey, msg, expired);
            return;
        }

        String time = LocalDateTime.now(ZoneId.of("Asia/Shanghai")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String appName = environment.getProperty("spring.application.name");
        String name = String.format("%s(%s)", gameId, appName);

        List<String> list = new ArrayList<>();
        list.add("游戏编号：" + name);
        list.add("本地时间：" + time);
        list.add("IP 地址：" + getLocalIP());
        list.add("错误内容：" + msg);
        if (extField != null && extField.size() >= 1) {
            list.addAll(extField);
        }
        String sendMsg = String.join("\n", list);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("chat_id", telegramConfig.getChatId());
        body.put("message", sendMsg);
        log.info("发送 telegram 报警. key:{}, msg:{}, config:{}", lockKey, body, JSON.toJSON(telegramConfig));
        HttpEntity<JSONObject> entity = new HttpEntity<>(body, headers);
        try {
            restTemplate.exchange(telegramConfig.getUrl(), HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            log.error("Telegram 报警发送异常:", e);
        }
    }
}
