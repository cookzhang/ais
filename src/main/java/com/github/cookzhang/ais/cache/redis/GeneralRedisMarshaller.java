package com.github.cookzhang.ais.cache.redis;

import com.github.cookzhang.ais.Callback;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.StatisticCenter;
import com.github.cookzhang.ais.cache.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;
import java.util.Date;

/**
 * User: zhangyi
 * Date: 4/8/14
 * Time: 17:25
 * Description:
 */
public class GeneralRedisMarshaller<K extends Payload, V extends Serializable> implements Marshaller<K, V>, Callback<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(GeneralRedisMarshaller.class);
    private JedisPool pool;

    public GeneralRedisMarshaller(String host, int port) {
        pool = RedisPool.getPool(host, port);
    }

    /**
     * 回调接口
     *
     * @param k 回调回传数据
     * @param v 回调回传数据
     */
    @Override
    public void call(K k, V v) {
        marshal(k, v);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V unmarshal(K k) {
        logger.debug("unmarshal K: {}", k);
        logger.info("general unmarshal operate start ......");
        RedisSerializer redisSerializer = new JdkSerializationRedisSerializer();
        Jedis jedis = null;
        byte[] content = new byte[0];
        try {
            jedis = pool.getResource();
            content = jedis.get(k.generate().getBytes());

            if (StatisticCenter.getInstance().getDetectors().containsKey(k.getLob())) {
                long now = new Date().getTime();
                jedis.expireAt(k.generate(), now + k.expire());
                logger.warn("expiry date extend because offline of the lob:{} ", k.getLob());
            }
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);   //释放redis对象
            logger.error(e.getMessage());
        } finally {
            RedisPool.returnResource(pool, jedis);  //返还到连接池
        }

        logger.info("general unmarshal operate finished");

        if (content ==null || content.length == 0) {
            logger.info("general unmarshal operate finished, not found cache.");
            return null;
        }

        return (V) redisSerializer.deserialize(content);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void marshal(K k, V v) {
        logger.debug("general marshal a key, value");
        logger.debug("Key:{}", k);
        //logger.debug("Value:{}", v);
        logger.info("general marshal operate start ......");
        RedisSerializer redisSerializer = new JdkSerializationRedisSerializer();
        byte[] content = redisSerializer.serialize(v);
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(k.generate().getBytes(), content);
            jedis.expire(k.generate(), k.expire());

        } catch (Exception e) {
            pool.returnBrokenResource(jedis);   //释放redis对象
            logger.error(e.getMessage());
        } finally {
            RedisPool.returnResource(pool, jedis);  //返还到连接池
        }

        logger.info("general marshal operate completed!");
    }
}
