package com.github.cookzhang.ais.queue.circle;

import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.queue.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: zhangyi
 * Date: 3/13/14
 * Time: 18:02
 * Description:
 */
@SuppressWarnings("unchecked")
public class CircularConsumer<T extends Payload> extends Thread {

    private CircularQueue<T> circularQueue = CircularQueue.getInstance();
    private MessageHandler<T> messageHandler;
    private static final Logger logger = LoggerFactory.getLogger(CircularConsumer.class);

    public void setMessageHandler(MessageHandler<T> messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        logger.info("circular queue consumer start......");
        while (true) {
            T t = circularQueue.poll();
            if (t == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {
                }

                continue;
            }

            logger.debug("消费一个队列消息:{}", t);
            logger.info("消费一个队列消息.");
            try {

                messageHandler.handleMessage(t);
            } catch (Exception e) {
                logger.error("处理队列消息出错: {}", e);
            }
        }
    }
}
