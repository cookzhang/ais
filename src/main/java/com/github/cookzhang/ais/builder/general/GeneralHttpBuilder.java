package com.github.cookzhang.ais.builder.general;

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
 * Modify:lubh
 * Date: 4/7/14
 * Time: 16:32
 * Description:
 */
public class GeneralHttpBuilder<V extends Serializable> extends AbstractGeneralBuilder<V> {

    private static final Logger logger = LoggerFactory.getLogger(GeneralHttpBuilder.class);

    /**
     * 动态Invoker生成方法
     *
     * @return 动态invoker
     */
    @Override
    public Invoker<V> createDynamic() {
        logger.info("create invoke general dynamic http starting ......");
        AbstractInvoker<V> abstractInvoker = new DynamicInvoker<V>(new HttpInvoker<V>());
        abstractInvoker.setMarshaller(marshaller);
        abstractInvoker.setPredicate(predicate);
        logger.info("create invoke general dynamic http completed!");

        return abstractInvoker;
    }

    /**
     * 静态Invoker生成方法
     *
     * @return 静态invoker
     */
    @Override
    public Invoker<V> createStatic() {
        logger.info("create invoke general static http starting ......");
        AbstractInvoker<V> abstractInvoker = new StaticInvoker<V>(new HttpInvoker<V>());
        abstractInvoker.setMarshaller(marshaller);
        abstractInvoker.setMessageProducer(messageProducer);
        abstractInvoker.setPredicate(predicate);
        logger.info("create invoke general static http completed!");

        return abstractInvoker;
    }
}
