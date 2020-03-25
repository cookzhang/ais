package com.github.cookzhang.ais.test;

import com.github.cookzhang.ais.Constants;
import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.OfflineSwitcher;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.invoker.HttpInvoker;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Date: 4/29/14
 * Time: 13:57
 * Description: 自动offline单元测试
 */
public class OfflineSwitcherTest extends TestCase {

    public void testSwitch() {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.SYSTEM_CONFIG_FILE);
        int threshold = Integer.parseInt(bundle.getString(Constants.OFFLINE_ERROR_THRESHOLD));

        Invoker<String> invoker = new HttpInvoker<String>();
        OfflineSwitcher switcher = OfflineSwitcher.getInstance();
        switcher.setInvoker(invoker);

        Payload payload = new Payload();
        payload.setLob("news");
        payload.setQueryString("www.autohome.com.cn");

        boolean first = false;
        for (int i = 0; i < threshold; i++) {
            if (i == threshold - 1) {
                first = switcher.isOpen(payload);
            }

            switcher.trigger(payload);
        }

        boolean result = switcher.isOpen(payload);

        Assert.assertTrue(!first && result);
    }
}
