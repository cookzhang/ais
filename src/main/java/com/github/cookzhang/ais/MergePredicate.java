
package com.github.cookzhang.ais;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class MergePredicate<E extends Payload> implements Serializable, Predicate<E> {

    private ResourceBundle resourceBundle;
    private static final Map<String, LoadingCache<Payload, Long>> caches = new HashMap<String, LoadingCache<Payload, Long>>();
    private static final Logger logger = LoggerFactory.getLogger(MergePredicate.class);

    public MergePredicate() {
        resourceBundle = ResourceBundle.getBundle(Constants.SYSTEM_CONFIG_FILE);
    }

    /**
     * 请求合并判断
     *
     * @param e 请求数据
     * @return 是否合并  返回 true则合并，false则不合并
     */
    @Override
    public boolean apply(E e) {
        LoadingCache<Payload, Long> cache = caches.get(e.getLob());

        if (cache == null) {
            cache = createLoadingCache(e);
            caches.put(e.getLob(), cache);
        }

        Long value = cache.getIfPresent(e);
        if (value == null) {
            cache.put(e, new Date().getTime());
            logger.debug("not merge invoke payload :{}", e);
            logger.debug("not merge invoke payload.");
            return false;
        }

        logger.debug("merge invoke payload :{}", e);
        logger.debug("merge invoke payload.");
        return true;
    }

    private LoadingCache<Payload, Long> createLoadingCache(Payload e) {
        int interval = e.getMergeInterval();
        if (interval <= 0) {
            interval = Integer.parseInt(resourceBundle.getString(Constants.MERGE_INTERVAL));
        }

        return CacheBuilder.newBuilder().expireAfterWrite(interval, TimeUnit.SECONDS)
                .maximumSize(Long.parseLong(resourceBundle.getString(Constants.MERGE_SPECIMEN)))
                .build(
                        new CacheLoader<Payload, Long>() {
                            public Long load(Payload payload) throws Exception {
                                return 0L;
                            }
                        }
                );
    }
}