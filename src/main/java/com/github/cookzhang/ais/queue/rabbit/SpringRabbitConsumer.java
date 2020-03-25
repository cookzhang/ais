package com.github.cookzhang.ais.queue.rabbit;

import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.queue.MessageConsumer;
import com.github.cookzhang.ais.queue.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: zhangyi
 * Date: 3/18/14
 * Time: 17:02
 * Description:
 */
public class SpringRabbitConsumer<T extends Payload> implements MessageConsumer<T> {

    private MessageHandler<T> messageHandler;
    private static final Logger logger = LoggerFactory.getLogger(SpringRabbitConsumer.class);

    public void setMessageHandler(MessageHandler<T> messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void onMessage(T t) {
        try {
            messageHandler.handleMessage(t);
        } catch (Exception e) {
            logger.error("handle message error: {}", e);
        }
    }

    @Override
    public T receive() {
        //同步消费消息
        return null;
    }
}
