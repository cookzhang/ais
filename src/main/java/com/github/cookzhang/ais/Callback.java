package com.github.cookzhang.ais;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/4/14
 * Time: 9:27
 * Description:
 */
public interface Callback<K extends Serializable, V extends Serializable> {

    /**
     * 回调接口
     *
     * @param k 回调数据
     * @param v 回调数据
     */
    public void call(K k, V v);
}
