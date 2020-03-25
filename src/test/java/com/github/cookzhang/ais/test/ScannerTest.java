package com.github.cookzhang.ais.test;

import com.github.cookzhang.ais.util.Scanner;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.List;

/**
 * User: zhangyi
 * Date: 5/5/14
 * Time: 9:23
 * Description:
 */
public class ScannerTest extends TestCase {

    public void testScanner() {
        List<String> clazzes = Scanner.getClassName("com.autohome.ais");
        for (String clazz : clazzes) {
            System.out.println(clazz);
        }
        System.out.println(clazzes.size());
        Assert.assertTrue(clazzes.size() > 0);
    }
}
