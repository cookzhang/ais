package com.github.cookzhang.ais.queue.rabbit;

import com.github.cookzhang.ais.Constants;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.cache.Marshaller;
import com.github.cookzhang.ais.cache.redis.GeneralRedisMarshaller;
import com.github.cookzhang.ais.queue.DefaultMessageHandler;
import com.github.cookzhang.ais.queue.MessageHandler;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Date: 4/14/14
 * Time: 17:15
 * Description:
 */
public class GeneralRabbitConsumer extends RabbitBase implements Runnable, Consumer {

    private ResourceBundle queueResource;
    private MessageHandler<Payload> messageHandler;
    private static final Logger logger = LoggerFactory.getLogger(GeneralRabbitConsumer.class);

    public GeneralRabbitConsumer(String host, int port) throws IOException {
        super(host, port);
        queueResource = ResourceBundle.getBundle(Constants.QUEUE_CONFIG_FILE);
        ResourceBundle cacheBundle = ResourceBundle.getBundle(Constants.CACHE_CONFIG_FILE);
        messageHandler = new DefaultMessageHandler<Payload>();  //设置默认消息处理器
        Marshaller<Payload, Serializable> marshaller = new GeneralRedisMarshaller<Payload, Serializable>(
                cacheBundle.getString(Constants.REDIS_HOST),
                Integer.parseInt(cacheBundle.getString(Constants.REDIS_PORT))
        );
        ((DefaultMessageHandler<Payload>) messageHandler).setMarshaller(marshaller);
    }

    public void setMessageHandler(MessageHandler<Payload> messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        logger.info("consumer {} registered", consumerTag);
    }

    @Override
    public void handleCancelOk(String consumerTag) {
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        while(!connection.isOpen()){
            try
            {
                connection();
                channel.basicConsume(queueResource.getString(Constants.RABBIT_QUEUE_NAME), true, this);
            }
            catch (Exception e) {
                if (connection.isOpen())
                {
                    try {
                        connection.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        try {
            messageHandler.handleMessage(Payload.deserialize(body));
        } catch (Exception e) {
            logger.error("handle message error: {}", e);
        }
    }

    @Override
    public void run() {
        try {
            channel.basicConsume(queueResource.getString(Constants.RABBIT_QUEUE_NAME), true, this);
        } catch (IOException e) {
            logger.error("message consumer start error: {}", e);
            throw new RuntimeException("message consumer start error", e);
        }
    }
}
