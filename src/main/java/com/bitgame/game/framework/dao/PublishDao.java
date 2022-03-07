package com.bitgame.game.framework.dao;

import com.bitgame.game.framework.config.game.GameConfig;
import com.bitgame.game.framework.constant.RedisConstant;
import com.bitgame.game.framework.model.RedisChannelModel;
import com.bitgame.game.framework.util.IpUtil;
import com.bitgame.game.framework.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class PublishDao {
    @Resource
    private GameConfig gameConfig;

    /**
     * 借助 Redis 实现消息广播
     *
     * @param users   接受消息用户
     * @param msgType 消息类型
     */
    public void publish(String[] users, String msgType) {
        publish(gameConfig.getId(), users, msgType);
    }

    /**
     * 借助 Redis 实现消息广播
     *
     * @param users   接受消息用户
     * @param msgType 消息类型
     * @param data    推送消息内容
     */
    public void publish(String[] users, String msgType, Object data) {
        publish(gameConfig.getId(), users, msgType, data);
    }

    /**
     * 借助 Redis 实现消息广播
     *
     * @param users   接受消息用户
     * @param msgType 消息类型
     */
    public void publish(String gameId, String[] users, String msgType) {
        publish(gameId, users, msgType, null);
    }

    /**
     * 借助 Redis 实现消息广播
     *
     * @param users   接受消息用户
     * @param msgType 消息类型
     * @param data    推送消息内容
     */
    public void publish(String gameId, String[] users, String msgType, Object data) {
        try {
            RedisChannelModel redisChannelModel = new RedisChannelModel();
            redisChannelModel.setGameId(gameId);
            redisChannelModel.setHostname(IpUtil.getIp());
            redisChannelModel.setMsgType(msgType);
            redisChannelModel.setUsers(users);
            redisChannelModel.setData(data);

            RedisUtil.publish(gameId, RedisConstant.CHANNEL_COMMUNICATION, redisChannelModel);
        } catch (Exception e) {
            log.error("Redis 发布消息异常:", e);
        }
    }
}
