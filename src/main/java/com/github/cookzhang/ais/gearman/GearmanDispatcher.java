package com.github.cookzhang.ais.gearman;

import com.github.cookzhang.ais.Dispatcher;
import org.gearman.Gearman;
import org.gearman.GearmanClient;
import org.gearman.GearmanJobEvent;
import org.gearman.GearmanJobReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/4/14
 * Time: 14:13
 * Description: Gearman调度器
 */
public class GearmanDispatcher implements Dispatcher {

    private GearmanClient client = null;
    private static final Logger logger = LoggerFactory.getLogger(GearmanDispatcher.class);

    public GearmanDispatcher(String host, int port) {
        try {
            Gearman gearman;
            gearman = Gearman.createGearman();
            client = gearman.createGearmanClient();
            final org.gearman.GearmanServer server = gearman.createGearmanServer(host, port);
            client.addServer(server);
        } catch (Exception e) {
            logger.error("instantiate GearmanDispatcher error:{}", e);
        }
    }

    /**
     * 调度方法
     *
     * @param payload 请求数据负载
     */
    @Override
    public Serializable submit(byte[] payload) {

        if (client == null) {
            logger.error("GearmanDispatcher not instance!");
            return null;
        }

        GearmanJobReturn jobReturn = client.submitJob(GeneralFunction.class.getCanonicalName(), payload);
        try {
            while (!jobReturn.isEOF()) {
                GearmanJobEvent event = jobReturn.poll();
                switch (event.getEventType()) {
                    case GEARMAN_JOB_SUCCESS: // Job completed successfully
                        logger.info("work return byte is success:");
                        return event.getData();
                    case GEARMAN_SUBMIT_FAIL: // The job submit operation failed
                        logger.info("The job submit operation failed");
                        break;
                    case GEARMAN_JOB_FAIL: // The job's execution failed
                        logger.info("The job's execution failed");
                        break;
                }
            }
        } catch (Exception e) {
            logger.error("gearman worker process error:{}", e.getMessage());
        }

        return null;
    }
}
