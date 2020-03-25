package com.github.cookzhang.ais.invoker.test;

import com.github.cookzhang.ais.HttpConverter;
import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.InvokerType;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.builder.Builder;
import com.github.cookzhang.ais.builder.general.GeneralHttpBuilder;
import com.github.cookzhang.ais.builder.spring.SpringHttpBuilder;
import com.github.cookzhang.ais.exception.ConvertException;
import com.github.cookzhang.ais.exception.InvokeException;
import junit.framework.TestCase;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;

import java.io.IOException;
import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/29/14
 * Time: 22:12
 * Description:
 */
public class HttpInvokerTest extends TestCase implements Serializable {

    public void testSpringStatic() {
        Builder<String> builder = new SpringHttpBuilder<String>();
        Invoker<String> invoker = builder.createStatic();
        Payload payload = new Payload();
        payload.setLob("news");
        payload.setExpire(60000);
        payload.setTimeout(20);
        payload.setType(InvokerType.HTTP);
        payload.setQueryString("www.sina.com.cn");

        try {
            String result = invoker.invoke(payload);
            Assert.assertTrue(result != null && result.length() > 0);
        } catch (InvokeException e) {
            e.printStackTrace();
        }
    }

    public void testSpringDynamic() {
        Builder<String> builder = new SpringHttpBuilder<String>();
        Invoker<String> invoker = builder.createDynamic();
        Payload payload = new Payload();
        payload.setLob("news");
        payload.setExpire(60000);
        payload.setTimeout(20);
        payload.setType(InvokerType.HTTP);
        payload.setQueryString("www.sina.com.cn");

        try {
            String result = invoker.invoke(payload);
            Assert.assertTrue(result != null && result.length() > 0);
        } catch (InvokeException e) {
            e.printStackTrace();
        }
    }

    public void testGeneralStatic() {
        Builder<String> builder = new GeneralHttpBuilder<String>();
        Invoker<String> invoker = builder.createStatic();
        final Payload payload = new Payload();
        payload.setLob("news");
        payload.setExpire(60000);
        payload.setTimeout(5000);
        payload.setType(InvokerType.HTTP);
        payload.setQueryString("http://temp.autohome.com.cn/default.aspx?test=2");
        String url ="http://temp.autohome.com.cn/default.aspx?test=2";
        int a = url.indexOf('?');
        String urlnop = url.substring(0,a);
        payload.setQueryStringNoParam(urlnop);
        try {
            String result ="";
            for (int i=0;i<1;i++){
                result = invoker.invoke(payload, new HttpConverter<String>() {
                    @Override
                    public String convert(HttpResponse httpResponse) throws ConvertException {
                        try {
                            return EntityUtils.toString(httpResponse.getEntity(), payload.getCharset());
                        } catch (IOException e) {
                            return null;
                        }
                    }
                });
            }
            Assert.assertTrue(result != null && result.length() > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testGeneralDynamic() {
        Builder<String> builder = new GeneralHttpBuilder<String>();
        Invoker<String> invoker = builder.createDynamic();
        Payload payload = new Payload();
        payload.setLob("news");
        payload.setExpire(60000);
        payload.setTimeout(20);
        payload.setType(InvokerType.HTTP);
        payload.setQueryString("www.sina.com.cn");

        try {
            String result = invoker.invoke(payload);
            Assert.assertTrue(result != null && result.length() > 0);
        } catch (InvokeException e) {
            e.printStackTrace();
        }
    }

    public void testConvert() {
        Builder<String> builder = new GeneralHttpBuilder<String>();
        Invoker<String> invoker = builder.createDynamic();
        final Payload payload = new Payload();
        payload.setLob("news");
        payload.setExpire(60000);
        payload.setTimeout(20);
        payload.setType(InvokerType.HTTP);
        payload.setQueryString("www.sina.com.cn");

        try {
            String result = invoker.invoke(payload, new HttpConverter<String>() {
                @Override
                public String convert(HttpResponse httpResponse) throws ConvertException {
                    try {
                        return EntityUtils.toString(httpResponse.getEntity(), payload.getCharset());
                    } catch (IOException e) {
                        return null;
                    }
                }
            });

            Assert.assertNotNull(result);
        } catch (InvokeException e) {
            e.printStackTrace();
        }
    }
}
