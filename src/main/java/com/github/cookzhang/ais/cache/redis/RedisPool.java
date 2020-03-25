package com.github.cookzhang.ais.cache.redis;

import com.github.cookzhang.ais.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Date: 4/9/14
 * Time: 19:54
 * Description:Redis池初始化类
 */
public class RedisPool {

    private static ResourceBundle cacheBundle = ResourceBundle.getBundle(Constants.CACHE_CONFIG_FILE);
    private static JedisPool pool;
    private static final Logger logger = LoggerFactory.getLogger(RedisPool.class);
    public static JedisPool getPool(String host, int port) {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(Integer.parseInt(cacheBundle.getString(Constants.REDIS_MAXACTIVE)));
            config.setMaxIdle(Integer.parseInt(cacheBundle.getString(Constants.REDIS_MAXIDLE)));
            config.setMaxWaitMillis(Integer.parseInt(cacheBundle.getString(Constants.REDIS_MAXWAITMILLIS)));
            config.setTestOnBorrow(true);
            pool = new JedisPool(config, host, port);
        }

        return pool;
    }

    /**
     * 返还到连接池
     *
     * @param pool  连接池
     * @param redis 实例
     */
    public static void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            pool.returnResource(redis);
        }
    }
}
