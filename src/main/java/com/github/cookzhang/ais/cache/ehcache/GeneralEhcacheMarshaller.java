package com.github.cookzhang.ais.cache.ehcache;

import com.github.cookzhang.ais.Payload;
import net.sf.ehcache.CacheManager;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/24/14
 * Time: 18:19
 * Description:
 */
public class GeneralEhcacheMarshaller<K extends Payload, V extends Serializable> extends AbstractEhcacheMarshaller<K, V> {

    @Override
    public void createCacheManager() {
        cacheManager = CacheManager.create();
    }
}
