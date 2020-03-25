package com.github.cookzhang.ais;

import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by zhangyi on 15/8/6.
 */
public class Test extends TestCase {
    public void test() throws IOException {
        Properties pps = new Properties();
        pps.load(new FileInputStream("/Users/zhangyi/work/code/conf/app/cashier/app.properties"));
        System.out.print(pps.getProperty("dalLoggerName"));
        ResourceBundle cacheBundle;
        cacheBundle = ResourceBundle.getBundle("/Users/zhangyi/work/code/conf/app/cashier/app.properties");
        cacheBundle.getString("dalLoggerName");
    }
}
