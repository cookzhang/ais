package com.github.cookzhang.ais.builder.spring;

import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.invoker.AbstractInvoker;
import com.github.cookzhang.ais.invoker.DynamicInvoker;
import com.github.cookzhang.ais.invoker.HttpInvoker;
import com.github.cookzhang.ais.invoker.StaticInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/7/14
 * Time: 16:21
 * Description: Spring实现版的HTTP类型调用的Builder
 */
public class SpringHttpBuilder<V extends Serializable> extends AbstractSpringBuilder<V> {

    private static final Logger logger = LoggerFactory.getLogger(SpringHttpBuilder.class);

    @SuppressWarnings("unchecked")
    @Override
    public Invoker<V> createDynamic() {
        logger.info("create invoke spring dynamic http starting ......");
        Invoker<V> httpInvoker = (Invoker<V>) context.getBean("httpInvoker", HttpInvoker.class);
        AbstractInvoker<V> abstractInvoker = new DynamicInvoker<V>(httpInvoker);
        abstractInvoker.setMarshaller(marshaller);
        abstractInvoker.setPredicate(predicate);
        logger.info("create invoke spring dynamic http completed!");

        return abstractInvoker;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Invoker<V> createStatic() {
        logger.info("create invoke spring static http starting ......");
        Invoker<V> httpInvoker = (Invoker<V>) context.getBean("httpInvoker", HttpInvoker.class);
        AbstractInvoker<V> abstractInvoker = new StaticInvoker<V>(httpInvoker);
        abstractInvoker.setMarshaller(marshaller);
        abstractInvoker.setMessageProducer(messageProducer);
        abstractInvoker.setPredicate(predicate);
        logger.info("create invoke spring static http completed!");

        return abstractInvoker;
    }
}
