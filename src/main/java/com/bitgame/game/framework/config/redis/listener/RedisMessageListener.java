package com.bitgame.game.framework.config.redis.listener;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.bitgame.game.framework.Client;
import com.bitgame.game.framework.ClientManager;
import com.bitgame.game.framework.ClientManagerMap;
import com.bitgame.game.framework.config.redis.RedisDispatcher;
import com.bitgame.game.framework.constant.SysRoute;
import com.bitgame.game.framework.model.RedisChannelModel;
import com.bitgame.game.framework.util.IpUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;


/**
 * Redis 订阅逻辑
 */
@Slf4j
@Component
public class RedisMessageListener implements MessageListener {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ClientManagerMap clientManagerMap;

    @Resource
    private RedisDispatcher redisDispatcher;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            MDC.put("X-B3-TraceId", IdUtil.objectId());

            RedisSerializer<?> valueSerializer = redisTemplate.getValueSerializer();
            RedisChannelModel redisChannelModel = (RedisChannelModel) valueSerializer.deserialize(message.getBody());
            if (redisChannelModel == null) {
                return;
            }

            log.debug("Redis 订阅接收数据. data:{}", JSON.toJSON(redisChannelModel));

            // 接受到的广播消息，本机消息则放弃处理
            if (IpUtil.getIp().equals(redisChannelModel.getHostname())) {
                return;
            }

            String gameId = redisChannelModel.getGameId();
            String msgType = redisChannelModel.getMsgType();
            String hostname = redisChannelModel.getHostname();
            String[] users = redisChannelModel.getUsers();
            Object data = redisChannelModel.getData();
            log.debug("接收消息 Redis 消息订阅 -  msgType:{}, hostname:{}, users:{}, data:{}", msgType, hostname, users, data);

            // 获取实例管理器对象
            ClientManager clientManager = clientManagerMap.get(gameId);
            if (clientManager == null) {
                log.error("消息订阅. 数据推送异常. clientManager 管理器异常");
                return;
            }

            // 跨服推送:
            // 1. 指定用户推送
            // 2. 推送所有用户
            // 未指定用户默认为: 推送所有用户
            if (SysRoute.PUBLISH.getCode().equals(msgType) && users.length <= 0) {
                users = clientManager.getUsers().toArray(new String[0]);
                log.debug("跨服推送:推送所有用户. users:{}", JSON.toJSON(users));
            }

            for (String userId : users) {
                MDC.put("user-id", userId);

                Client client = clientManager.getClientByUserId(userId);
                if (client == null) {
                    log.debug("跨服消息推送:未找到指定 websocket 对象. userId:{}", userId);
                    continue;
                }

                log.debug("跨服消息推送:开始执行消息推送. userId:{}", userId);
                try {
                    // 跨服推送:异地登陆
                    if (SysRoute.ALREADY_LOGIN.getCode().equals(msgType)) {
                        redisDispatcher.alreadyLogin(client);
                    }

                    // 跨服推送:
                    // 1. 指定用户推送
                    // 2. 推送所有用户
                    if (SysRoute.PUBLISH.getCode().equals(msgType)) {
                        redisDispatcher.publish(client, data.toString());
                    }
                } catch (Exception e) {
                    log.error("Redis 消息订阅 - 执行异常. msgType:{}, data:{}, err:", msgType, data, e);
                }
            }
        } catch (Exception e) {
            log.error("Redis 消息订阅 - 异常. err:", e);
        } finally {
            MDC.clear();
        }
    }
}

