package com.github.cookzhang.ais.queue;

import com.autohome.ais.*;
import com.github.cookzhang.ais.GlobalExecutor;
import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.InvokerRegistry;
import com.github.cookzhang.ais.OfflineSwitcher;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.Switcher;
import com.github.cookzhang.ais.cache.Marshaller;
import com.github.cookzhang.ais.exception.InvokeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 3/18/14
 * Time: 11:45
 * Description:
 */
public class DefaultMessageHandler<T extends Payload> implements MessageHandler<T> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageHandler.class);
    private InvokerRegistry<? extends Serializable> registry = InvokerRegistry.getInstance();
    private Marshaller<Payload, Serializable> marshaller;
    private Switcher switcher = OfflineSwitcher.getInstance();

    public void setMarshaller(Marshaller<Payload, Serializable> marshaller) {
        this.marshaller = marshaller;
    }

    public void setInvokerRegistry(InvokerRegistry<? extends Serializable> invokerRegistry) {
        this.registry = invokerRegistry;
    }

    public void handleMessage(final T t) throws Exception {
        GlobalExecutor.getBackground().execute(new Runnable() {
            @Override
            public void run() {
                Invoker<? extends Serializable> invoker = registry.findInvoker(t.getType());
                if (invoker == null) {
                    logger.error("not found {} invoker, please check your config file", t.getType());
                    return;
                }

                try {
                    if (switcher.isOpen(t)) {
                        logger.warn("lob of {} has offline status.", t.getLob());
                        return;
                    }

                    @SuppressWarnings("unchecked")
                    Serializable result = invoker.invoke(t, t.getConverter());
                    marshaller.marshal(t, result);
                } catch (InvokeException e) {
                    switcher.setInvoker(invoker);
                    switcher.trigger(t);
                    logger.error("async invoker error:{}", e);
                }
            }
        });
    }
}
