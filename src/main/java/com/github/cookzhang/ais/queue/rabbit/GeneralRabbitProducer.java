package com.github.cookzhang.ais.queue.rabbit;

import com.github.cookzhang.ais.Constants;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.queue.MessageProducer;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Date: 4/9/14
 * Time: 10:32
 * Description:
 */
public class GeneralRabbitProducer<T extends Payload> extends RabbitBase implements MessageProducer<T> {

    String queueName;
    String exchange;
    String routingKey;
    private static final Logger logger = LoggerFactory.getLogger(GeneralRabbitProducer.class);

    public GeneralRabbitProducer(String host, int port) throws IOException {
        super(host, port);
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.QUEUE_CONFIG_FILE);
        queueName = bundle.getString(Constants.RABBIT_QUEUE_NAME);
        exchange = bundle.getString(Constants.RABBIT_QUEUE_EXCHANGE);
        routingKey = bundle.getString(Constants.RABBIT_QUEUE_ROUTINGKEY);
        createChannel();
    }

    private void createChannel () throws IOException
    {
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, exchange, routingKey);
    }

    @Override
    public void send(T t) {
        try {
            if (!connection.isOpen())
            {
                logger.error("retry connetion rabbitmq");
                connection();
                createChannel();
            }
            channel.basicPublish("", queueName, MessageProperties.BASIC, t.serialize());
        } catch (Exception e) {
            logger.error("send message error: {}", e);
        }
    }
}
