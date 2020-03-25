package com.github.cookzhang.ais.example;

import com.autohome.ais.*;
import com.github.cookzhang.ais.builder.Builder;
import com.github.cookzhang.ais.builder.spring.SpringDatabaseBuilder;
import com.github.cookzhang.ais.builder.spring.SpringHttpBuilder;
import com.github.cookzhang.ais.exception.InvokeException;
import com.github.cookzhang.ais.DatabaseConverter;
import com.github.cookzhang.ais.HttpConverter;
import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.InvokerType;
import com.github.cookzhang.ais.Payload;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.ResultSet;

/**
 * User: zhangyi
 * Date: 4/7/14
 * Time: 16:39
 * Description:
 */
public class DynamicApplication {

    private static final Logger logger = LoggerFactory.getLogger(DynamicApplication.class);

    public static void main(String[] args) {
        DynamicApplication application = new DynamicApplication();
        application.databaseTest();
    }

    public void httpTest() {
        Builder<String> builder = new SpringHttpBuilder<String>();//spring方式HTTP调用
        //Builder<String> builder = new GeneralHttpBuilder(); //普通方式HTTP调用
        //动态调用
        Invoker<String> invoker = builder.createDynamic();
        Payload payload = new Payload();
        payload.setLob("club");
        payload.setProtocol("http");
        payload.setExpire(1000);
        payload.setMergeInterval(200);
        payload.setType(InvokerType.HTTP);
        payload.setPort(80);
        payload.setQueryString("http://www.sina.com.cn");

        for (int i = 0; i < 5; i++) {
            try {
                String result = invoker.invoke(payload, new HttpConverter<String>() {
                    @Override
                    public String convert(HttpResponse response) {
                        try {
                            return EntityUtils.toString(response.getEntity());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }
                });
                logger.debug(result);
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            } catch (InvokeException e) {
                logger.error("http dynamic invoke error:{}", e);
            }
        }
    }

    public void databaseTest() {
        Builder<String> builder = new SpringDatabaseBuilder<String>();//spring方式HTTP调用
        //Builder<String> builder = new GeneralDatabaseBuilder<String>();
        Invoker<String> invoker = builder.createDynamic();
        Payload payload = new Payload();
        payload.setLob("club");
        payload.setProtocol("db");
        payload.setExpire(10000);
        payload.setMergeInterval(200);
        payload.setType(InvokerType.DATABASE);
        payload.setPort(80);
        payload.setQueryString("select * from user");

        for (int i = 0; i < 5; i++) {
            try {
                String result = invoker.invoke(payload, new DatabaseConverter<String>() {
                    @Override
                    public String convert(ResultSet resultSet) {
                        Gson gson = new Gson();
                        return gson.toJson(resultSet);
                    }
                });
                logger.debug(result);
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {

            } catch (InvokeException e) {
                logger.info("database dynamic invoke error:{}", e);
            }
        }
    }
}
