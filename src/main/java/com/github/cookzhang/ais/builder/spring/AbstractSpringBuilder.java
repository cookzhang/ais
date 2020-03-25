package com.github.cookzhang.ais.builder.spring;

import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.Predicate;
import com.github.cookzhang.ais.builder.Builder;
import com.github.cookzhang.ais.cache.Marshaller;
import com.github.cookzhang.ais.queue.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/7/14
 * Time: 12:03
 * Description:从Spring中加载、生成相应的Marshaller、MessageProducer以及相应的Invoker等bean
 */
public abstract class AbstractSpringBuilder<T extends Serializable> implements Builder<T> {

    protected static ApplicationContext context;
    protected Marshaller<Payload, T> marshaller;
    protected Predicate<Payload> predicate;
    protected MessageProducer<Payload> messageProducer;

    private static final Logger logger = LoggerFactory.getLogger(AbstractSpringBuilder.class);

    static {
        logger.info("initialize spring context starting ......");
        context = new ClassPathXmlApplicationContext("ais-core.xml");
        logger.info("initialize spring context completed!");
    }

    @SuppressWarnings("unchecked")
    protected AbstractSpringBuilder() {
        logger.info("fetch marshaller instant from spring.");
        marshaller = context.getBean("marshaller", Marshaller.class);
        logger.info("fetch message producer instant from spring.");
        messageProducer = context.getBean("messageProducer", MessageProducer.class);
        logger.info("fetch  instant from spring.");
        predicate = context.getBean("predicate", Predicate.class);
    }

    @Override
    public void makeMarshaller() {
    }

    @Override
    public void makeMessageProducer() {
    }

    @Override
    public void makePredicate() {
    }
}
