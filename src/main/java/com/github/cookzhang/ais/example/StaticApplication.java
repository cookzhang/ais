package com.github.cookzhang.ais.example;

import com.github.cookzhang.ais.HttpConverter;
import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.InvokerType;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.builder.Builder;
import com.github.cookzhang.ais.builder.general.GeneralDatabaseBuilder;
import com.github.cookzhang.ais.builder.general.GeneralHttpBuilder;
import com.github.cookzhang.ais.exception.InvokeException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * User: zhangyi
 * Date: 4/7/14
 * Time: 16:39
 * Description:
 */
public class StaticApplication {

    private static final Logger logger = LoggerFactory.getLogger(StaticApplication.class);

    public static void main(String[] args) {

        StaticApplication application = new StaticApplication();
        application.databaseTest();
    }

    public void httpTest() {
        //Builder<String> builder = new SpringHttpBuilder<String>(); //spring方式HTTP调用
        Builder<String> builder = new GeneralHttpBuilder<String>(); //普通方式HTTP调用

        //动态调用
        Payload payload = new Payload();
        payload.setLob("club");
        payload.setProtocol("http");
        payload.setExpire(1000);
        payload.setMergeInterval(200);
        payload.setType(InvokerType.HTTP);
        payload.setPort(80);
        payload.setQueryString("http://www.sina.com.cn");

        Invoker<String> invoker = builder.createStatic();

        for (int i = 0; i < 15; i++) {
            try {
                invoker.invoke(payload, new HttpConverter<String>() {
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
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            } catch (InvokeException e) {
                logger.error("http static invoke error:{}", e);
            }
        }
    }

    public void databaseTest() {
        //Builder<String> builder = new SpringDatabaseBuilder<String>(); //spring方式Database调用
        Builder<String> builder = new GeneralDatabaseBuilder<String>();//普通方式Database调用
        Invoker<String> invoker = builder.createStatic();

        //动态调用
        Payload payload = new Payload();
        payload.setLob("club.static");
        payload.setProtocol("db");
        payload.setExpire(10);
        payload.setMergeInterval(5);
        payload.setType(InvokerType.DATABASE);
        payload.setPort(80);
        payload.setQueryString("select * from user");

        for (int i = 0; i < 50; i++) {
            try {
                /*String result = invoker.invoke(payload, new DatabaseConverter<String>() {
                    @Override
                    public String convert(ResultSet resultSet) throws ConvertException {
                        List<Map<String, String>> list = Lists.newArrayList();

                        try {
                            // 获取列数
                            ResultSetMetaData metaData = resultSet.getMetaData();
                            int columnCount = metaData.getColumnCount();

                            // 遍历ResultSet中的每条数据
                            while (resultSet.next()) {
                                Map<String, String> meta = Maps.newHashMap();

                                // 遍历每一列
                                for (int i = 1; i <= columnCount; i++) {
                                    String columnName = metaData.getColumnLabel(i);
                                    String value = resultSet.getString(columnName);
                                    meta.put(columnName, value);
                                }
                                list.add(meta);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Gson gson = new Gson();
                        return gson.toJson(list);
                    }
                });*/

                String result = invoker.invoke(payload);

                System.out.println(result);

                Thread.sleep(1000L);
            } catch (InterruptedException ignored) {
            } catch (InvokeException e) {
                logger.error("database static invoke error:{}", e);
            }
        }
    }
}