package com.github.cookzhang.ais.queue.rabbit;

import com.github.cookzhang.ais.Constants;
import com.github.cookzhang.ais.gearman.GearmanDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Date: 5/22/14
 * Time: 18:02
 * Description: general monitor 的启动入口
 */
public class GeneralMonitorBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(GeneralMonitorBootstrap.class);

    public static void main(String[] args) {
        ResourceBundle rabbitBundle = ResourceBundle.getBundle(Constants.QUEUE_CONFIG_FILE);
        ResourceBundle gearmanBundle = ResourceBundle.getBundle(Constants.GEARMAN_CONFIG_FILE);

        String rabbitHost = rabbitBundle.getString(Constants.RABBIT_HOST);
        int rabbitPort = Integer.parseInt(rabbitBundle.getString(Constants.RABBIT_PORT));

        String gearmanHost = gearmanBundle.getString(Constants.GEARMAN_HOST);
        int gearmanPort = Integer.parseInt(gearmanBundle.getString(Constants.GEARMAN_PORT));

        try {
            logger.info("general rabbitmq monitor start......");

            GeneralRabbitMonitor monitor = new GeneralRabbitMonitor(rabbitHost, rabbitPort);
            GearmanDispatcher dispatcher = new GearmanDispatcher(gearmanHost, gearmanPort);
            monitor.setDispatcher(dispatcher);
            monitor.start();
        } catch (IOException e) {
            logger.error("general rabbitmq monitor start error:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
