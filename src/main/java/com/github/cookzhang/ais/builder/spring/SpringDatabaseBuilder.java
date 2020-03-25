package com.github.cookzhang.ais.builder.spring;

import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.invoker.AbstractInvoker;
import com.github.cookzhang.ais.invoker.DynamicInvoker;
import com.github.cookzhang.ais.invoker.StaticInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/7/14
 * Time: 16:25
 * Description:Spring实现版的Database类型调用的Builder
 */
public class SpringDatabaseBuilder<V extends Serializable> extends AbstractSpringBuilder<V> {

    private static final Logger logger = LoggerFactory.getLogger(SpringDatabaseBuilder.class);

    @SuppressWarnings("unchecked")
    @Override
    public Invoker<V> createDynamic() {
        logger.info("create invoke spring dynamic database starting ......");
        Invoker<V> databaseInvoker = (Invoker<V>) context.getBean("databaseInvoker", Invoker.class);
        AbstractInvoker<V> abstractInvoker = new DynamicInvoker<V>(databaseInvoker);
        abstractInvoker.setMarshaller(marshaller);
        abstractInvoker.setPredicate(predicate);
        logger.info("create invoke spring dynamic database completed!");

        return abstractInvoker;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Invoker<V> createStatic() {
        logger.info("create invoke spring static database starting ......");
        Invoker<V> databaseInvoker = (Invoker<V>) context.getBean("databaseInvoker", Invoker.class);
        AbstractInvoker<V> abstractInvoker = new StaticInvoker<V>(databaseInvoker);
        abstractInvoker.setMarshaller(marshaller);
        abstractInvoker.setMessageProducer(messageProducer);
        abstractInvoker.setPredicate(predicate);
        logger.info("create invoke spring static database completed!");

        return abstractInvoker;
    }
}
