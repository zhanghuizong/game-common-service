# 1. Redis

## 1.1. KeyPrefixSerializer 自定义序列化规则

操作 Redis 是会自动增加 `key` 前缀

**前缀生成规则**

${spring.application.name}:${game.gameId}:${spring.profiles.active}:


# 2. websocket 通讯规则

地址格式: `ws://localhost:7018/ws?auth=asfasdfasdfjskflsfajlfajlkfajsklfas`

`auth` 客户端随机加密生成. 目的用于后续每次通讯数据传递(加密解密)

> 当然可通过配置文件调整. 支持明文传输. 例如在压测环境下可采用该方式

# 3. 框架功能

- websocket 路由注册与分发
- 异地登陆检测
- 通讯加解密





