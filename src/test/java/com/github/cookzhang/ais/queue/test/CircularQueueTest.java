package com.github.cookzhang.ais.queue.test;

import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.queue.MessageHandler;
import com.github.cookzhang.ais.queue.MessageProducer;
import com.github.cookzhang.ais.queue.circle.CircularConsumer;
import com.github.cookzhang.ais.queue.circle.CircularProducer;
import junit.framework.TestCase;
import org.junit.Assert;
import java.io.IOException;
/**
 * User: zhangyi
 * Modify: lubh
 * Date: 4/17/14
 * Time: 12:44
 * Description: 环形队列单元测试
 */
public class CircularQueueTest extends TestCase {

    public void testProducer() {
        Payload payload = new Payload();
        payload.setLob("news");
        payload.setQueryString("www.autohome.com.cn");
        MessageProducer<Payload> producer;
        try {
            producer = new CircularProducer<Payload>();
            producer.send(payload);
        } catch (IOException e) {
            fail("exception");
        }
        Assert.assertTrue(true);
    }

    public void testConsumer() {
        try {
            CircularConsumer<Payload> consumer = new CircularConsumer<Payload>();
            consumer.setMessageHandler(new MessageHandler<Payload>() {
                @Override
                public void handleMessage(Payload message) throws Exception {
                    System.out.println(message);
                    Assert.assertTrue(true);
                }
            });
            consumer.start();
        } catch (Exception e) {
            fail("exception");
        }
        Assert.assertTrue(true);
    }

    public void testProducerAndConsumer(){

        Payload payload = new Payload();
        payload.setLob("news");
        payload.setQueryString("www.autohome.com.cn");
        MessageProducer<Payload> producer;
        CircularConsumer<Payload> consumer;
        try {
            producer = new CircularProducer<Payload>();
            producer.send(payload);
            consumer = new CircularConsumer<Payload>();
            consumer.setMessageHandler(new MessageHandler<Payload>() {
                @Override
                public void handleMessage(Payload message) throws Exception {
                    message.setQueryString("test");
                }
            });
            consumer.start();
        } catch (IOException e) {
            fail("exception");
        }
        Assert.assertTrue(true);
    }
}
