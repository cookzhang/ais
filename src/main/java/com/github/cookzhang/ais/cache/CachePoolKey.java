package com.github.cookzhang.ais.cache;

/**
 * User: lubh
 * Date: 4/23/14
 * Time: 13:09
 * Description: 获取缓存池标记位！
 */
public interface CachePoolKey {

    public String generate(String ip,int port);

}
