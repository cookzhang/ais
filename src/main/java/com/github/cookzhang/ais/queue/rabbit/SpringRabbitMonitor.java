package com.github.cookzhang.ais.queue.rabbit;

import com.github.cookzhang.ais.Constants;
import com.github.cookzhang.ais.Dispatcher;
import com.github.cookzhang.ais.Monitor;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Date: 4/5/14
 * Time: 20:41
 * Description:
 */
public class SpringRabbitMonitor extends ConsumerAdapter implements Monitor, ChannelCallback<String> {

    private AMQP.Queue.DeclareOk declareOk;
    private final ResourceBundle queueResource;
    private Dispatcher dispatcher;
    private String consumerTag;
    private RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(SpringRabbitMonitor.class);

    public SpringRabbitMonitor(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        queueResource = ResourceBundle.getBundle(Constants.QUEUE_CONFIG_FILE);
        declareOk = rabbitTemplate.execute((new ChannelCallback<AMQP.Queue.DeclareOk>() {
            public AMQP.Queue.DeclareOk doInRabbit(Channel channel) throws Exception {
                return channel.queueDeclarePassive(queueResource.getString(Constants.RABBIT_QUEUE_NAME));
            }
        }));
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * 开启监控
     * 每分钟监控一次队列消息数
     */
    @Override
    public void start() {
        Thread monitorWork = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    logger.info("monitor scan once.");
                    int threshold = Integer.parseInt(queueResource.getString(Constants.RABBIT_QUEUE_MONITOR_THRESHOLD));
                    if (declareOk.getConsumerCount() == 0 || declareOk.getMessageCount() > threshold) {
                        logger.warn("queue {} has no consumer or message count over {}", queueResource.getString(Constants.RABBIT_QUEUE_NAME), threshold);
                        registerConsumer();
                    } else {
                        cancelConsumer();
                    }

                    try {
                        Thread.sleep(30000);    //每半分钟，监控扫描一次队列
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });

        monitorWork.start();
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        dispatcher.submit(body);
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        logger.info("consumer {} registered.", consumerTag);
    }

    private void registerConsumer() {
        if (consumerTag != null) {
            return;
        }

        consumerTag = rabbitTemplate.execute(this);
        logger.info("monitor register consumer to process queue message.");
    }

    private void cancelConsumer() {
        if (consumerTag == null) {
            return;
        }

        rabbitTemplate.execute(new ChannelCallback<Object>() {
            @Override
            public Object doInRabbit(Channel channel) throws Exception {
                channel.basicCancel(consumerTag);
                return null;
            }
        });
        consumerTag = null;
        logger.info("monitor unregister consumer to cancel process queue message.");
    }

    @Override
    public String doInRabbit(Channel channel) throws Exception {
        channel.basicQos(10);
        return channel.basicConsume(queueResource.getString(Constants.RABBIT_QUEUE_NAME), true, this);
    }
}
