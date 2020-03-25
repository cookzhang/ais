package com.github.cookzhang.ais;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Modify:lubh
 * Date: 4/1/14
 * Time: 12:10
 * Description:默认offline策略-当连续N次请求源失败时，则自动启动offline
 */
public class OfflineSwitcher implements Switcher {

    private static final Logger logger = LoggerFactory.getLogger(OfflineSwitcher.class);
    private static volatile boolean autoOffline;
    private StatisticCenter statisticCenter = StatisticCenter.getInstance();
    private Invoker invoker;

    private OfflineSwitcher() {
    }

    public static OfflineSwitcher getInstance() {
        return SwitchHolder.instance;
    }

    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    /**
     * 隔离方法
     *
     * @param payload 业务请求数据负载
     * @return 是否实现offline隔离
     */
    @Override
    public boolean isOpen(Payload payload) {
        return statisticCenter.getDetectors().get(payload.getLob()) != null && autoOffline;
    }

    /**
     * 当发生源请求错误时，激发该方法
     * 目前该种方法是只有在访问源失败时，读取一次配置信息
     *
     * @param payload 业务请求负载
     */
    @Override
    public void trigger(Payload payload) {

        if (invoker == null) {
            logger.error("invoker is null");
            return;
        }

        Detector detector = statisticCenter.getDetectors().get(payload.getLob());
        if (detector != null) {
            logger.warn("log of {} was offline, detector is working.");
            return;
        }

        logger.info("OfflineSwitch trigger once, lob is:{}", payload.getLob());

        ResourceBundle.clearCache();
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.SYSTEM_CONFIG_FILE);
        int flag = Integer.parseInt(bundle.getString(Constants.OFFLINE_SWITCH));

        logger.info("OfflineSwitch config flag is:{}", flag);

        if (flag == 0) {
            autoOffline = false;  //不开启offline
            return;
        }

        if (flag == 1) {
            autoOffline = true;   //自动offline
        }

        int threshold = Integer.parseInt(bundle.getString(Constants.OFFLINE_ERROR_THRESHOLD));
        logger.info("OfflineSwitch threshold is:{}", threshold);

        int count = statisticCenter.getErrorCount(payload);

        if (count >= threshold) { //自动开启offline
            detector = new Detector(invoker, this);
            statisticCenter.getDetectors().put(payload.getLob(), detector);

            detector.detect(payload);
            statisticCenter.resetCount(payload);
            logger.warn("error count has over threshold, offline open automatically.  lob is:{}", payload.getLob());
            return;
        }

        statisticCenter.increment(payload);
    }

    /**
     * 关闭offline功能
     *
     * @param payload 业务负载
     */
    @Override
    public void close(Payload payload) {
        statisticCenter.resetCount(payload);
        statisticCenter.getDetectors().remove(payload.getLob());
    }

    private static class SwitchHolder {
        private static OfflineSwitcher instance = new OfflineSwitcher();
    }
}
