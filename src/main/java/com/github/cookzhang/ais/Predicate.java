package com.github.cookzhang.ais;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/8/14
 * Time: 15:42
 * Description: 请求合并判断接口
 */
public interface Predicate<E extends Serializable> {

    /**
     * 请求合并判断
     *
     * @param e 请求数据
     * @return 是否合并  返回 true则合并，false则不合并
     */
    public boolean apply(E e);
}
