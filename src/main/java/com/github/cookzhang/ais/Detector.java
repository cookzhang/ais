package com.github.cookzhang.ais;

import com.github.cookzhang.ais.exception.InvokeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Date: 4/1/14
 * Time: 13:56
 * Description: offlineSwitch 自动启用后，即刻启动自动恢复探测器，一旦发现源服务器正常之后即关闭offline
 */
public class Detector {

    private static final Logger logger = LoggerFactory.getLogger(Detector.class);
    private final Invoker invoker;
    private final Switcher offlineSwitch;
    private ResourceBundle bundle = ResourceBundle.getBundle(Constants.SYSTEM_CONFIG_FILE);

    public Detector(Invoker invoker, Switcher offlineSwitch) {
        this.invoker = invoker;
        this.offlineSwitch = offlineSwitch;
    }

    /**
     * 自动探测源服务器可用状况，当连续N次请求均正常时，则关闭offline
     *
     * @param payload 请求数据负载
     */
    @SuppressWarnings("unchecked")
    public void detect(final Payload payload) {
        final int threshold = Integer.parseInt(bundle.getString(Constants.OFFLINE_RESTORE_THRESHOLD));
        GlobalExecutor.getBackground().submit(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (true) {
                    logger.warn("automatic detector working......");

                    try {
                        invoker.invoke(payload, Converters.nullConverter());
                        if (count > threshold) {
                            logger.warn("offline was closed automatically by detector. lob is:{}", payload.getLob());
                            offlineSwitch.close(payload);
                            return;
                        }

                        logger.warn("offline is continuing. log is:{}", payload.getLob());
                        count++;
                    } catch (InvokeException e) {
                        count = 0;
                        logger.error(e.getMessage());
                    }

                    try {
                        Thread.sleep(5000);    //请求间隔为1分钟
                    } catch (InterruptedException ignored) {
                        logger.error(ignored.getMessage());
                    }
                }
            }
        });
    }
}
