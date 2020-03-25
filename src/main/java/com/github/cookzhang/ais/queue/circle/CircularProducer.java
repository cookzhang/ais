package com.github.cookzhang.ais.queue.circle;

import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.queue.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: zhangyi
 * Date: 3/13/14
 * Time: 17:35
 * Description:
 */
public class CircularProducer<T extends Payload> implements MessageProducer<T> {

    private CircularQueue<T> circularQueue = CircularQueue.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(CircularProducer.class);

    public void send(T t) {
        logger.debug("send a message:{}", t);
        logger.info("send a message starting...");
        circularQueue.push(t);
        logger.info("send a message completed.");
    }
}
