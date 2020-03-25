package com.github.cookzhang.ais.cache;

import com.github.cookzhang.ais.Payload;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 3/12/14
 * Time: 20:09
 * Description: 缓存对象处理接口： 序列化/反序列化
 */
public interface Marshaller<K extends Payload, V extends Serializable> {

    public V unmarshal(K k);

    public void marshal(K k, final V v);
}
