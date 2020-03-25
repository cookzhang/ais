package com.github.cookzhang.ais.invoker;

import com.github.cookzhang.ais.Converter;
import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.exception.InvokeException;
import com.github.cookzhang.ais.log.AisLog;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * User: zhangyi
 * Date: 3/21/14
 * Time: 17:45
 * Description: 静态业务调用器
 */
public class StaticInvoker<T extends Serializable> extends AbstractInvoker<T> {

    private static final Logger logger = LoggerFactory.getLogger(StaticInvoker.class);

    public StaticInvoker(Invoker<T> invoker) {
        super(invoker);
    }

    /**
     * 异步请求接口
     * 优先从缓存中获取缓存数据，如果缓存存在，则直接返回，并进行异步接口调用
     * 如果没有缓存数据，则同步调用，得到返回数据放进缓存并将该数据返回
     *
     * @param payload 业务负载数据
     * @return 返回请求结果
     */
    @Override
    public T invoke(Payload payload) throws InvokeException {
        return invoke(payload, null);
    }

    /**
     * 执行请求
     *
     * @param payload   业务负载数据
     * @param converter 数据类型转换器
     * @return 返回请求结果
     */
    @Override
    public <S> T invoke(Payload payload, Converter<S, T> converter) throws InvokeException {
        logger.info("static invoke starting......");

        payload.setConverter(converter);
        //缓存响应时间
        Stopwatch watch  = new Stopwatch();
        watch.start();
        T t = marshaller.unmarshal(payload);
        long nanos = watch.elapsed(TimeUnit.MILLISECONDS);
        watch.stop();
        AisLog.getInstance().appendLog("cacheResponseTime@{}`{}`{}",
                payload.getLob(),
                payload.getQueryStringNoParam(),
                nanos);
        if (isOffline(payload)) {
            logger.warn("lob of {} has offline status.", payload.getLob());
            return t;
        }

        if (t != null) {
            logger.info("invoke hit cache, async send a message to queue.");
            AisLog.getInstance().appendLog("cacheHitRate@{}`{}`{}",
                    payload.getLob(),
                    payload.getQueryStringNoParam(),
                    1);
            payload.setConverter(converter);
            asyncEnqueue(payload);
            logger.info("static invoke completed.");
            return t;
        }

        try {
            logger.info("no cache, invoke source directly");
            AisLog.getInstance().appendLog("cacheHitRate@{}`{}`{}",
                    payload.getLob(),
                    payload.getQueryStringNoParam(),
                    0);
            t = invoker.invoke(payload, converter);
        } catch (InvokeException e) {
            logger.error("invoke error:{}", e.getMessage());
            offlineSwitch.trigger(payload);
            throw new InvokeException("invoke error", e);
        }

        asyncFlushCache(payload, t);
        logger.info("static invoke completed.");
        return t;
    }
}
