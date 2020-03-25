package com.github.cookzhang.ais.cache.test;

import com.github.cookzhang.ais.Constants;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.cache.Marshaller;
import com.github.cookzhang.ais.cache.ehcache.GeneralEhcacheMarshaller;
import com.github.cookzhang.ais.cache.redis.GeneralRedisMarshaller;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Date: 4/29/14
 * Time: 17:57
 * Description:
 */
public class GeneralMarshallerTest extends TestCase {

    public void testEhcacheMarshaller() {
        Marshaller<Payload, String> marshaller = new GeneralEhcacheMarshaller<Payload, String>();

        Payload payload = new Payload();
        payload.setLob("news");
        payload.setExpire(2);
        payload.setQueryString("www.autohome.com.cn/abc/");
        String before = marshaller.unmarshal(payload);
        marshaller.marshal(payload, "abc");

        String first = null, second = null;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                first = marshaller.unmarshal(payload);
            }

            if (i == 2) {
                second = marshaller.unmarshal(payload);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }

        Assert.assertTrue(before == null && first != null && second == null);
    }

    public void testRedisMarshaller() {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.CACHE_CONFIG_FILE);
        String host = bundle.getString(Constants.REDIS_HOST);
        int port = Integer.parseInt(bundle.getString(Constants.REDIS_PORT));

        Marshaller<Payload, String> marshaller = new GeneralRedisMarshaller<Payload, String>(host, port);

        Payload payload = new Payload();
        payload.setLob("news");
        payload.setExpire(2);
        payload.setQueryString("www.autohome.com.cn/abc/");
        String before = marshaller.unmarshal(payload);
        marshaller.marshal(payload, "abc");

        String first = null, second = null;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                first = marshaller.unmarshal(payload);
            }

            if (i == 2) {
                second = marshaller.unmarshal(payload);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }

        Assert.assertTrue(before == null && first != null && second == null);
    }
}
