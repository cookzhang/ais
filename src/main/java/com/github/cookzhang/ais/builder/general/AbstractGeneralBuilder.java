package com.github.cookzhang.ais.builder.general;

import com.autohome.ais.*;
import com.github.cookzhang.ais.Constants;
import com.github.cookzhang.ais.ManualRegistryInitializer;
import com.github.cookzhang.ais.MergePredicate;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.Predicate;
import com.github.cookzhang.ais.RegistryInitializer;
import com.github.cookzhang.ais.builder.Builder;
import com.github.cookzhang.ais.cache.Marshaller;
import com.github.cookzhang.ais.cache.ehcache.GeneralEhcacheMarshaller;
import com.github.cookzhang.ais.queue.DefaultMessageHandler;
import com.github.cookzhang.ais.queue.MessageProducer;
import com.github.cookzhang.ais.queue.circle.CircularConsumer;
import com.github.cookzhang.ais.queue.circle.CircularProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Modify:lubh
 * Date: 4/7/14
 * Time: 15:42
 * Description: 根据配置文件读取出系统所需要的资源，生成相应的Marshaller、MessageProducer以及相应的Invoker
 */
@SuppressWarnings("unchecked")
public abstract class AbstractGeneralBuilder<T extends Serializable> implements Builder<T> {
    private ResourceBundle cacheBundle;
    private ResourceBundle queueBundle;
    protected Marshaller<Payload, T> marshaller;
    protected MessageProducer<Payload> messageProducer;
    protected Predicate<Payload> predicate;
    private static final Logger logger = LoggerFactory.getLogger(AbstractGeneralBuilder.class);

    protected AbstractGeneralBuilder() {
        logger.debug("init GeneralBuilder...");
        RegistryInitializer initializer = new ManualRegistryInitializer();
        initializer.initialize();
        cacheBundle = ResourceBundle.getBundle(Constants.CACHE_CONFIG_FILE);
        queueBundle = ResourceBundle.getBundle(Constants.QUEUE_CONFIG_FILE);
        makeMarshaller();
        makeMessageProducer();
        makePredicate();
        startConsumer();
    }

    @Override
    public final void makeMarshaller() {
        marshaller = new GeneralEhcacheMarshaller<Payload, T>();
        //String host = cacheBundle.getString(Constants.REDIS_HOST);
        //int port = Integer.parseInt(cacheBundle.getString(Constants.REDIS_PORT));
        //marshaller = new GeneralRedisMarshaller<Payload, T>(host,port);
    }

    @Override
    public final void makeMessageProducer() {
        messageProducer = new CircularProducer<Payload>();
        //String host = queueBundle.getString(Constants.RABBIT_HOST);
       //int port = Integer.parseInt(queueBundle.getString(Constants.RABBIT_PORT));
       // try {
         //   messageProducer = new GeneralRabbitProducer<Payload>(host,port);
        //} catch (IOException e) {
         //   e.printStackTrace();
        //}
    }

    @Override
    public final void makePredicate() {
        predicate = new MergePredicate<Payload>();
    }

    protected void startConsumer() {
        DefaultMessageHandler<Payload> handler = new DefaultMessageHandler<Payload>();

        handler.setMarshaller((Marshaller<Payload, Serializable>)marshaller);
        CircularConsumer<Payload> circularConsumer = new CircularConsumer<Payload>();
        circularConsumer.setMessageHandler(handler);
        Thread consumer = new Thread(circularConsumer);
        consumer.start();

//        DefaultMessageHandler<Payload> handler = new DefaultMessageHandler<Payload>();
//
//        handler.setMarshaller((Marshaller<Payload, Serializable>)marshaller);
//
//        String host = queueBundle.getString(Constants.RABBIT_HOST);
//        int port = Integer.parseInt(queueBundle.getString(Constants.RABBIT_PORT));
//        try {
//            com.autohome.ais.queue.rabbit.GeneralRabbitConsumer rbConsumer =
//                    new com.autohome.ais.queue.rabbit.GeneralRabbitConsumer(host,port);
//            rbConsumer.setMessageHandler(handler);
//            Thread consumer = new Thread(rbConsumer);
//            consumer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
