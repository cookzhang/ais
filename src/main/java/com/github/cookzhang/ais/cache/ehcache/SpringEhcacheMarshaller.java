package com.github.cookzhang.ais.cache.ehcache;

import com.github.cookzhang.ais.Payload;
import net.sf.ehcache.CacheManager;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/24/14
 * Time: 18:13
 * Description:
 */
public class SpringEhcacheMarshaller<K extends Payload, V extends Serializable> extends AbstractEhcacheMarshaller<K, V> {

    @Override
    public void createCacheManager() {
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
