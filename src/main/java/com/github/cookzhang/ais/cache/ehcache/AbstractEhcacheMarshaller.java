package com.github.cookzhang.ais.cache.ehcache;

import com.github.cookzhang.ais.Callback;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.StatisticCenter;
import com.github.cookzhang.ais.cache.Marshaller;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/24/14
 * Time: 17:26
 * Description:
 */
public abstract class AbstractEhcacheMarshaller<K extends Payload, V extends Serializable> implements Marshaller<K, V>, Callback<K, V> {

    protected CacheManager cacheManager;
    private static final Logger logger = LoggerFactory.getLogger(AbstractEhcacheMarshaller.class);

    protected AbstractEhcacheMarshaller() {
        createCacheManager();

    }

    public abstract void createCacheManager();

    /**
     * 回调接口
     *
     * @param k 回调数据
     * @param v 回调数据
     */
    @Override
    public void call(K k, V v) {
        logger.info("receive a callback.");
        marshal(k, v);
    }

    @Override
    public V unmarshal(K k) {
        logger.debug("unmarshal K: {}", k);
        logger.info("general unmarshal operate start ......");
        Ehcache cache = cacheManager.getEhcache(k.getLob());
        if (cache == null) {
            logger.info("ehcache unmarshal operate finished, not found lob cache.");
            return null;
        }

        Element element = cache.get(k.generate());
        if (element == null || element.isExpired()) {
            logger.info("ehcache unmarshal operate finished, not found cache.");
            return null;
        }

        if (StatisticCenter.getInstance().getDetectors().containsKey(k.getLob())) {
            element.setTimeToLive(k.expire());
            logger.warn("expiry date extend because offline of the lob:{} ", k.getLob());
            logger.info("ehcache unmarshal operate finished");
            @SuppressWarnings("unchecked")
            V v = (V) element.getObjectValue();
            return v;
        }

        @SuppressWarnings("unchecked")
        V v = (V) element.getObjectValue();
        logger.info("ehcache unmarshal operate finished");
        return v;
    }

    @Override
    public void marshal(K k, V v) {
        Ehcache cache = cacheManager.addCacheIfAbsent(k.getLob());
        Element element = new Element(k.generate(), v, true);
        element.setTimeToLive(k.expire());
        cache.put(element);
        Element e = cache.get(k.generate());
        logger.debug("Ehcache: the number of elements currently in the Cache {} ",cache.getSize());
    }


}
