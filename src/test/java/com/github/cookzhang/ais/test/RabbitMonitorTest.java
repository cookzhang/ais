package com.github.cookzhang.ais.test;

import com.github.cookzhang.ais.Constants;
import com.github.cookzhang.ais.Dispatcher;
import com.github.cookzhang.ais.gearman.GearmanDispatcher;
import com.github.cookzhang.ais.queue.rabbit.GeneralRabbitMonitor;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Date: 5/4/14
 * Time: 13:49
 * Description:
 */
public class RabbitMonitorTest extends TestCase {

    public void testMonitor() {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.QUEUE_CONFIG_FILE);
        ResourceBundle gearmanResource = ResourceBundle.getBundle(Constants.GEARMAN_CONFIG_FILE);

        String host = bundle.getString(Constants.RABBIT_HOST);
        int port = Integer.parseInt(bundle.getString(Constants.RABBIT_PORT));
        try {
            Dispatcher dispatcher = new GearmanDispatcher(gearmanResource.getString(Constants.GEARMAN_HOST), Integer.parseInt(gearmanResource.getString(Constants.GEARMAN_PORT)));
            GeneralRabbitMonitor monitor = new GeneralRabbitMonitor(host, port);
            monitor.setDispatcher(dispatcher);
            monitor.start();

            Thread.sleep(2000);

            Assert.assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
