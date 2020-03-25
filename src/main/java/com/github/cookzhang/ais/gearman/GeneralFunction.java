package com.github.cookzhang.ais.gearman;

import com.github.cookzhang.ais.Callback;
import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.InvokerRegistry;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.exception.InvokeException;
import org.gearman.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/4/14
 * Time: 10:56
 * Description:
 */
public class GeneralFunction implements GearmanFunction {

    private Callback callback;
    private final GearmanServer server;
    private final GearmanWorker worker;
    private InvokerRegistry registry = InvokerRegistry.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(GeneralFunction.class);

    public GeneralFunction(String host, int port) {
        Gearman gearman = Gearman.createGearman();
        server = gearman.createGearmanServer(host, port);
        worker = gearman.createGearmanWorker();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void start() {
        logger.info("gearman work start......");
        worker.addFunction(getClass().getCanonicalName(), this);
        worker.addServer(server);
    }

    @SuppressWarnings("unchecked")
    @Override
    public byte[] work(String function, byte[] bytes, GearmanFunctionCallback gearmanFunctionCallback) throws Exception {

        logger.info("gearman worker received a event.");
        Payload payload = Payload.deserialize(bytes);

        try {
            Invoker<Serializable> invoker = registry.findInvoker(payload.getType());
            if (invoker == null) {
                logger.error("can not found matched invoker of {}", payload.getType());
                return new byte[0];
            }

            Serializable result = invoker.invoke(payload, payload.getConverter());
            callback.call(payload, result);
            logger.info("gearman worker process a event succeed.");
        } catch (InvokeException e) {
            logger.error("invoke error {}", e.getMessage());
        }
        return new byte[0];
    }
}
