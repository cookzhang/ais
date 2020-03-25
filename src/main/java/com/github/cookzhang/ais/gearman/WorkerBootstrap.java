package com.github.cookzhang.ais.gearman;


import com.autohome.ais.*;
import com.github.cookzhang.ais.cache.ehcache.GeneralEhcacheMarshaller;
import com.github.cookzhang.ais.Callback;
import com.github.cookzhang.ais.Constants;
import com.github.cookzhang.ais.ManualRegistryInitializer;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.RegistryInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Date: 4/3/14
 * Time: 17:55
 * Description:
 */
public class WorkerBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(WorkerBootstrap.class);

    public static void main(String[] args) {
        logger.info("one gearman worker start......");
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.GEARMAN_CONFIG_FILE);
        String host = bundle.getString(Constants.GEARMAN_HOST);
        int port = Integer.parseInt(bundle.getString(Constants.GEARMAN_PORT));
        RegistryInitializer initializer = new ManualRegistryInitializer();
        initializer.initialize();

        Callback<Payload, Serializable> callback = new GeneralEhcacheMarshaller<Payload, Serializable>();
        GeneralFunction generalFunction = new GeneralFunction(host, port);
        generalFunction.setCallback(callback);

        generalFunction.start();
    }
}
