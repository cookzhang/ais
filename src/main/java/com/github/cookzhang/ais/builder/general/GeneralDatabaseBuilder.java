package com.github.cookzhang.ais.builder.general;

import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.invoker.AbstractInvoker;
import com.github.cookzhang.ais.invoker.DynamicInvoker;
import com.github.cookzhang.ais.invoker.GeneralDatabaseInvoker;
import com.github.cookzhang.ais.invoker.StaticInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * User: zhangyi
 * Modify:lubh
 * Date: 4/7/14
 * Time: 16:37
 * Description:
 */
public class GeneralDatabaseBuilder<V extends Serializable> extends AbstractGeneralBuilder<V> {

    private static final Logger logger = LoggerFactory.getLogger(GeneralDatabaseBuilder.class);

    /**
     * 动态Invoker生成方法
     *
     * @return 动态invoker
     */
    @Override
    public Invoker<V> createDynamic() {
        logger.info("create invoke general dynamic database starting ......");
        AbstractInvoker<V> abstractInvoker = new DynamicInvoker<V>(new GeneralDatabaseInvoker<V>());
        abstractInvoker.setMarshaller(marshaller);
        abstractInvoker.setPredicate(predicate);

        return abstractInvoker;
    }

    /**
     * 静态Invoker生成方法
     *
     * @return 静态invoker
     */
    @Override
    public Invoker<V> createStatic() {
        logger.info("create invoke general static database starting ......");
        AbstractInvoker<V> abstractInvoker = new StaticInvoker<V>(new GeneralDatabaseInvoker<V>());
        abstractInvoker.setMarshaller(marshaller);
        abstractInvoker.setMessageProducer(messageProducer);
        abstractInvoker.setPredicate(predicate);
        logger.info("create invoke general static database completed!");

        return abstractInvoker;
    }
}
