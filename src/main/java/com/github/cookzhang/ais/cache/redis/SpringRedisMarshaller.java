package com.github.cookzhang.ais.cache.redis;


import com.github.cookzhang.ais.Callback;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.StatisticCenter;
import com.github.cookzhang.ais.cache.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.Serializable;
import java.util.Date;

/**
 * User: zhangyi
 * Date: 3/12/14
 * Time: 20:14
 * Description:
 */
public class SpringRedisMarshaller<K extends Payload, V extends Serializable> implements Marshaller<K, V>, Callback<K, V> {

    private RedisTemplate<Serializable, Serializable> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(SpringRedisMarshaller.class);

    public void setRedisTemplate(RedisTemplate<Serializable, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V unmarshal(final K k) {
        logger.debug("unmarshal K: {}", k);
        logger.info("general unmarshal operate start ......");
        return redisTemplate.execute(new RedisCallback<V>() {
            @Override
            public V doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer redisSerializer = new JdkSerializationRedisSerializer();
                if (!connection.exists(k.generate().getBytes())) {
                    logger.info("spring unmarshal, not found cache.");
                    return null;
                }

                byte[] value = connection.get(k.generate().getBytes());

                if (value == null) {
                    logger.info("spring unmarshal operate finished, not found cache.");
                    return null;
                }

                if (StatisticCenter.getInstance().getDetectors().containsKey(k.getLob())) {
                    long now = new Date().getTime();
                    connection.expireAt(k.generate().getBytes(), now + k.expire());
                    logger.warn("expiry date extend because offline of the lob:{} ", k.getLob());
                }

                logger.info("spring unmarshal operate finished.");
                return (V) redisSerializer.deserialize(value);
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void marshal(final K k, final V v) {
        logger.debug("spring marshal a key, value");
        logger.debug("Key:{}", k);
        logger.debug("Value:{}", v);
        logger.info("spring marshal operate start ......");
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer redisSerializer = new JdkSerializationRedisSerializer();
                connection.set(k.generate().getBytes(), redisSerializer.serialize(v));
                connection.expire(k.generate().getBytes(), k.expire());
                return null;
            }

        });

        logger.info("spring marshal operate completed!");
    }

    /**
     * 回调接口
     *
     * @param k 回调回传数据
     * @param v 回调回传数据
     */
    @Override
    public void call(K k, V v) {
        logger.info("async update cache. ------ ");
        marshal(k, v);
    }
}

