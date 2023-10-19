package vip.mango2.mangocore.Utils;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import vip.mango2.mangocore.Entity.RedisConfig;

public class RedisUtils {
    private JedisPool jedisPool;

    public RedisUtils(RedisConfig redisConfig) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        if (StringUtils.isNotBlank(redisConfig.getPassword())) {
            jedisPool = new JedisPool(jedisPoolConfig, redisConfig.getHost(), redisConfig.getPort(), 10000, redisConfig.getPassword());
            return;
        }
        jedisPool = new JedisPool(jedisPoolConfig, redisConfig.getHost(), redisConfig.getPort());
    }

    // 设置键值对
    public void set(String key, String value) {
        jedisPool.getResource().set(key, value);
    }

    // 获取键值对
    public String get(String key) {
        return jedisPool.getResource().get(key);
    }

    // 删除键值对
    public void del(String key) {
        jedisPool.getResource().del(key);
    }

    // 判断键是否存在
    public boolean exists(String key) {
        return jedisPool.getResource().exists(key);
    }

    // 设置键值对并设置过期时间
    public void setex(String key, int seconds, String value) {
        jedisPool.getResource().setex(key, seconds, value);
    }

    // 设置键值对并设置过期时间
    public void setex(String key, int seconds, Object value) {
        jedisPool.getResource().setex(key, seconds, String.valueOf(value));
    }

    public void close() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}
