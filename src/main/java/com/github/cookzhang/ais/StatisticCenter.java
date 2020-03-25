package com.github.cookzhang.ais;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: zhangyi
 * Modify:lubh
 * Date: 4/1/14
 * Time: 17:28
 * Description: 调用统计中心
 */
public class StatisticCenter {

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private Map<String, Detector> detectors = new ConcurrentHashMap<String, Detector>();

    public static StatisticCenter getInstance() {
        return StatisticCenterHolder.instance;
    }

    private static class StatisticCenterHolder {
        private static StatisticCenter instance = new StatisticCenter();
    }

    private Map<String, LoadingCache<String, Integer>> cacheMap;

    private StatisticCenter() {
        cacheMap = Maps.newConcurrentMap();
    }

    public int getErrorCount(Payload payload) {
        readLock.lock();
        try {
            LoadingCache<String, Integer> cache = cacheMap.get(payload.getLob());
            if (cache == null) {
                cache = CacheBuilder.newBuilder().expireAfterWrite(payload.getErrorTimes(), TimeUnit.SECONDS)
                        .build(
                                new CacheLoader<String, Integer>() {
                                    public Integer load(String payload) throws Exception {
                                        return 1;
                                    }
                                }
                        );

                cacheMap.put(payload.getLob(), cache);
            }

            return cache.get(payload.getLob());
        } catch (ExecutionException e) {
            return 0;
        } finally {
            readLock.unlock();
        }
    }

    public void resetCount(Payload payload) {
        writeLock.lock();
        try {
            LoadingCache<String, Integer> cache = cacheMap.get(payload.getLob());
            cache.put(payload.getLob(), 0);
        } finally {
            writeLock.unlock();
        }
    }

    public void increment(Payload payload) {
        writeLock.lock();
        try {
            LoadingCache<String, Integer> cache = cacheMap.get(payload.getLob());

            int preCount = cache.get(payload.getLob());
            cache.put(payload.getLob(), preCount + 1);
        } catch (ExecutionException ignored) {
        } finally {
            writeLock.unlock();
        }
    }

    public Map<String, Detector> getDetectors() {
        return detectors;
    }
}
