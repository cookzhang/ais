package com.github.cookzhang.ais.queue;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 3/13/14
 * Time: 18:02
 * Description:
 */
public interface MessageConsumer<T extends Serializable> {

    public T receive();

    public void onMessage(T t);
}
