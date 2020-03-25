package com.github.cookzhang.ais.cache.redis;

import com.github.cookzhang.ais.cache.CachePoolKey;

/**
 * User: lubh
 * Date: 4/9/14
 * Time: 19:54
 * Description:
 */
public class RedisPoolKey implements CachePoolKey {

    @Override
    public String generate(String ip, int port) {
        StringBuilder keyBuilder = new StringBuilder();

        if(ip != null){
            keyBuilder.append(ip);
        }

        keyBuilder.append("_");
        keyBuilder.append(String.valueOf(port));

        return keyBuilder.toString();
    }

}
