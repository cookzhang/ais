package com.github.cookzhang.ais.invoker.test;

import com.github.cookzhang.ais.DatabaseConverter;
import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.InvokerType;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.builder.Builder;
import com.github.cookzhang.ais.builder.general.GeneralDatabaseBuilder;
import com.github.cookzhang.ais.builder.spring.SpringDatabaseBuilder;
import com.github.cookzhang.ais.exception.ConvertException;
import com.github.cookzhang.ais.exception.InvokeException;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: zhangyi
 * Date: 4/29/14
 * Time: 22:12
 * Description:
 */
public class DatabaseInvokerTest extends TestCase {

    public void testSpringStatic() {
        Builder<String> builder = new SpringDatabaseBuilder<String>();
        Invoker<String> invoker = builder.createStatic();
        Payload payload = new Payload();
        payload.setLob("club");
        payload.setExpire(10000);
        payload.setMergeInterval(20);
        payload.setType(InvokerType.DATABASE);
        payload.setQueryString("select * from user");

        try {
            String result = invoker.invoke(payload);
            System.out.println(result);


            Assert.assertTrue(result != null && result.length() > 0);
        } catch (InvokeException e) {
            e.printStackTrace();
        }
    }

    public void testSpringDynamic() {
        Builder<String> builder = new SpringDatabaseBuilder<String>();
        Invoker<String> invoker = builder.createDynamic();
        Payload payload = new Payload();
        payload.setLob("club");
        payload.setExpire(10000);
        payload.setMergeInterval(20);
        payload.setType(InvokerType.DATABASE);
        payload.setQueryString("select * from user");

        try {
            String result = invoker.invoke(payload);
            Assert.assertTrue(result != null && result.length() > 0);
        } catch (InvokeException e) {
            e.printStackTrace();
        }
    }

    public void testGeneralStatic() {
        Builder<String> builder = new GeneralDatabaseBuilder<String>();
        Invoker<String> invoker = builder.createStatic();
        Payload payload = new Payload();
        payload.setLob("club");
        payload.setExpire(10000);
        payload.setMergeInterval(20);
        payload.setType(InvokerType.DATABASE);
        payload.setQueryString("select * from user");

        try {
            String result = invoker.invoke(payload);
            Assert.assertTrue(result != null && result.length() > 0);
        } catch (InvokeException e) {
            e.printStackTrace();
        }
    }

    public void testGeneralDynamic() {
        Builder<String> builder = new GeneralDatabaseBuilder<String>();
        Invoker<String> invoker = builder.createDynamic();
        Payload payload = new Payload();
        payload.setLob("club");
        payload.setExpire(10000);
        payload.setMergeInterval(20);
        payload.setType(InvokerType.DATABASE);
        payload.setQueryString("select * from user");

        try {
            String result = invoker.invoke(payload);
            Assert.assertTrue(result != null && result.length() > 0);
        } catch (InvokeException e) {
            e.printStackTrace();
        }
    }

    public void testConvert() {
        Builder<ArrayList<User>> builder = new GeneralDatabaseBuilder<ArrayList<User>>();
        Invoker<ArrayList<User>> invoker = builder.createDynamic();
        Payload payload = new Payload();
        payload.setLob("club");
        payload.setExpire(10000);
        payload.setMergeInterval(20);
        payload.setType(InvokerType.DATABASE);
        payload.setQueryString("select * from user");

        try {
            List<User> users = invoker.invoke(payload, new DatabaseConverter<ArrayList<User>>() {
                @Override
                public ArrayList<User> convert(ResultSet resultSet) throws ConvertException {

                    ArrayList<User> users = new ArrayList<User>();
                    try {
                        while (resultSet.next()) {
                            User user = new User();
                            user.setId(resultSet.getInt("id"));
                            user.setAge(resultSet.getInt("age"));
                            user.setName(resultSet.getString("name"));

                            users.add(user);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    return users;
                }
            });

            Assert.assertTrue(users.size() > 0);
        } catch (InvokeException e) {
            e.printStackTrace();
        }
    }

    private class User implements Serializable {
        private int id;
        private int age;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}