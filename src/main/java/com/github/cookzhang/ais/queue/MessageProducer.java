package com.github.cookzhang.ais.queue;

import com.github.cookzhang.ais.Payload;

import java.io.IOException;

/**
 * User: zhangyi
 * Date: 3/13/14
 * Time: 17:32
 * Description:
 */
public interface MessageProducer<T extends Payload> {

    public void send(T t) throws IOException;
}
