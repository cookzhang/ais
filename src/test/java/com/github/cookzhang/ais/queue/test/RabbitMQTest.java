package com.github.cookzhang.ais.queue.test;

import com.github.cookzhang.ais.Constants;
import com.github.cookzhang.ais.InvokerType;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.queue.MessageHandler;
import com.github.cookzhang.ais.queue.rabbit.GeneralRabbitConsumer;
import com.github.cookzhang.ais.queue.rabbit.GeneralRabbitProducer;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Date: 4/29/14
 * Time: 18:12
 * Description:
 */
public class RabbitMQTest extends TestCase {

    public void testProducer() {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.QUEUE_CONFIG_FILE);
        String host = bundle.getString(Constants.RABBIT_HOST);
        int port = Integer.parseInt(bundle.getString(Constants.RABBIT_PORT));

        Payload payload = new Payload();
        payload.setLob("news");
        payload.setType(InvokerType.HTTP);
        payload.setQueryString("www.sina.com.cn");
        GeneralRabbitProducer<Payload> producer = null;
        try {
            producer = new GeneralRabbitProducer<Payload>(host, port);

            for (int i = 0; i < 5; i++) {
                producer.send(payload);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            fail("exception");
        } finally {
            if (producer != null) {
                producer.close();
            }
        }

        Assert.assertTrue(true);
    }

    public void testConsumer() {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.QUEUE_CONFIG_FILE);
        String host = bundle.getString(Constants.RABBIT_HOST);
        int port = Integer.parseInt(bundle.getString(Constants.RABBIT_PORT));

        try {
            GeneralRabbitConsumer consumer = new GeneralRabbitConsumer(host, port);
            consumer.setMessageHandler(new MessageHandler<Payload>() {
                @Override
                public void handleMessage(Payload message) throws Exception {
                    System.out.println(message);
                }
            });

            Thread thread = new Thread(consumer);
            thread.start();

            Thread.sleep(200000);
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail("exception");
        }

        Assert.assertTrue(true);
    }
}
