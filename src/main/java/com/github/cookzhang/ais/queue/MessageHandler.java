package com.github.cookzhang.ais.queue;

import com.github.cookzhang.ais.Payload;

/**
 * User: zhangyi
 * Date: 3/17/14
 * Time: 16:34
 * Description: 消息处理器
 */
public interface MessageHandler<T extends Payload> {

    public void handleMessage(T t) throws Exception;
}
