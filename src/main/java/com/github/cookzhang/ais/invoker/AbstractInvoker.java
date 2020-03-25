package com.github.cookzhang.ais.invoker;

import com.autohome.ais.*;
import com.github.cookzhang.ais.cache.Marshaller;
import com.github.cookzhang.ais.queue.MessageProducer;
import com.github.cookzhang.ais.GlobalExecutor;
import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.OfflineSwitcher;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.Predicate;
import com.github.cookzhang.ais.Switcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/1/14
 * Time: 11:25
 * Description:
 */
public abstract class AbstractInvoker<V extends Serializable> implements Invoker<V> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractInvoker.class);
    protected Invoker<V> invoker;
    protected Marshaller<Payload, V> marshaller;
    protected Switcher offlineSwitch;
    protected Predicate<Payload> predicate;
    protected MessageProducer<Payload> messageProducer;

    public AbstractInvoker(Invoker<V> invoker) {
        this.invoker = invoker;
        offlineSwitch = OfflineSwitcher.getInstance();
        offlineSwitch.setInvoker(invoker);
      }

    public void setMarshaller(Marshaller<Payload, V> marshaller) {
        this.marshaller = marshaller;
    }

    public void setMessageProducer(MessageProducer<Payload> messageProducer) {
        this.messageProducer = messageProducer;
    }

    public void setPredicate(Predicate<Payload> predicate) {
        this.predicate = predicate;
    }

    /**
     * 更新缓存中的数据【一定要异步，否则阻塞请求访问返回速度，可用线程执行】,更新缓存不需要做去重处理！
     *
     * @param payload 请求负载
     * @param v       缓存数据
     */
    protected void asyncFlushCache(final Payload payload, final V v) {
        GlobalExecutor.getFront().execute(new Runnable() {
            @Override
            public void run() {
                logger.debug("async flush cache of payload:{}", payload);
                logger.info("async flush cache of payload starting......");
                marshaller.marshal(payload, v);
                logger.info("async flush cache of payload succeed.");
            }
        });
    }

    /**
     * 异步更新缓存【一定要异步，否则阻塞请求访问返回速度，可用线程执行】
     *
     * @param payload 消息
     */
    protected void asyncEnqueue(final Payload payload) {
        GlobalExecutor.getFront().execute(new Runnable() {
            @Override
            public void run() {
                if (predicate.apply(payload)) {
                    logger.info("async message was merged.");
                    return;
                }

                logger.debug("async send a message to queue :{}", payload);
                logger.info("async message was not merged.");

                try {
                    messageProducer.send(payload);
                    logger.info("send a message to queue succeed.");
                } catch (IOException e) {
                    logger.error("send message error: {}", e);
                }
            }
        });
    }

    /**
     * 是否启用了offline
     *
     * @param payload 请求负载
     * @return true:offline/false:online
     */
    protected boolean isOffline(Payload payload) {
        return offlineSwitch.isOpen(payload);
    }
}
