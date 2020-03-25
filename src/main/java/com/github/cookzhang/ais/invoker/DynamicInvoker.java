package com.github.cookzhang.ais.invoker;

import com.github.cookzhang.ais.Converter;
import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.exception.InvokeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 3/21/14
 * Time: 17:45
 * Description: 动态业务调用器
 */
public class DynamicInvoker<T extends Serializable> extends AbstractInvoker<T> {

    private static final Logger logger = LoggerFactory.getLogger(DynamicInvoker.class);

    public DynamicInvoker(Invoker<T> invoker) {
        super(invoker);
    }

    /**
     * 执行请求
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
        logger.info("dynamic invoke starting......");

        payload.setConverter(converter);
        if (isOffline(payload)) {
            logger.warn("lob of {} has offline status.", payload.getLob());
            return marshaller.unmarshal(payload);
        }

        T t;
        try {
            t = invoker.invoke(payload, converter);
        } catch (InvokeException e) {
            logger.error("invoke error:{}", e.getMessage());
            logger.warn("fetch cache for return data.");
            offlineSwitch.trigger(payload);
            t = marshaller.unmarshal(payload);
            if (t == null) {
                throw new InvokeException("invoke error", e);
            }
        }

        asyncFlushCache(payload, t);
        logger.info("dynamic invoke completed.");
        return t;
    }
}
