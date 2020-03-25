package com.github.cookzhang.ais.log;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: zhangyi
 * Date: 2014/11/20
 * Time: 10:11
 * Description:
 */
public class AisLog {
    private static final Logger logger = LoggerFactory.getLogger(AisLog.class);
    public static AisLog getInstance() {
        return SwitchHolder.instance;
    }

    private static class SwitchHolder {
        private static AisLog instance = new AisLog();
    }

    public void appendLog(String format, Object... args)
    {
        logger.info(format, args);
    }
}
