package com.github.cookzhang.ais.cache.test;

import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.cache.Marshaller;
import junit.framework.TestCase;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: zhangyi
 * Date: 4/29/14
 * Time: 17:38
 * Description: 缓存单元测试
 */
public class SpringMarshallerTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testMarshaller() {
        ApplicationContext context = new ClassPathXmlApplicationContext("ais-core.xml");
        Marshaller<Payload, String> marshaller = (Marshaller<Payload, String>) context.getBean("marshaller", Marshaller.class);

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
