package com.github.cookzhang.ais.queue.rabbit;

import com.github.cookzhang.ais.Constants;
import com.github.cookzhang.ais.Dispatcher;
import com.github.cookzhang.ais.Monitor;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Date: 4/5/14
 * Time: 20:41
 * Description:
 */
public class GeneralRabbitMonitor extends RabbitBase implements Monitor, Consumer {

    private AMQP.Queue.DeclareOk declareOk;
    private final ResourceBundle queueResource;
    private Dispatcher dispatcher;
    private String consumerTag;
    private static final Logger logger = LoggerFactory.getLogger(GeneralRabbitMonitor.class);

    public GeneralRabbitMonitor(String host, int port) throws IOException {
        super(host, port);
        queueResource = ResourceBundle.getBundle(Constants.QUEUE_CONFIG_FILE);
        declareOk = channel.queueDeclare(queueResource.getString(Constants.RABBIT_QUEUE_NAME), false, false, true, null);
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
                    //是否启动gearman分发
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
    public void handleConsumeOk(String consumerTag) {
        logger.info("consumer {} registered.", consumerTag);
    }

    @Override
    public void handleCancelOk(String consumerTag) {
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        logger.info("dispatch a message to distribute processor.");
        dispatcher.submit(body);
    }

    private void registerConsumer() {
        try {
            if (consumerTag != null) {
                return;
            }

            consumerTag = channel.basicConsume(queueResource.getString(Constants.RABBIT_QUEUE_NAME), true, this);
            channel.basicQos(10);
            logger.info("monitor register consumer to process queue message.");
        } catch (IOException e) {
            logger.error("register consumer error:{}", e.getMessage());
        }
    }

    private void cancelConsumer() {
        try {
            if (consumerTag == null) {
                return;
            }

            channel.basicCancel(consumerTag);
            consumerTag = null;
            logger.info("monitor unregister consumer to cancel process queue message.");
        } catch (IOException e) {
            logger.error("unregister consumer error:{}", e.getMessage());
        }

    }
}
