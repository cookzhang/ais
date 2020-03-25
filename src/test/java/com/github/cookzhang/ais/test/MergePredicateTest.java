package com.github.cookzhang.ais.test;

import com.github.cookzhang.ais.MergePredicate;
import com.github.cookzhang.ais.Payload;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * User: zhangyi
 * Date: 4/15/14
 * Time: 11:20
 * Description: 合并判断器单元测试
 */
public class MergePredicateTest extends TestCase {

    private MergePredicate<Payload> predicate = new MergePredicate<Payload>();

    public void testPredicate() {
        Payload payload = new Payload();
        payload.setLob("news.index");
        payload.setMergeInterval(3);
        payload.setQueryString("www.autohome.com.cn");

        boolean first = false;
        boolean second = false;
        boolean third = false;
        boolean last = false;

        for (int i = 0; i <= 3; i++) {
            boolean result = predicate.apply(payload);
            if (i == 0) {
                first = result;
            }
            if (i == 1) {
                second = result;
            }
            if (i == 2) {
                third = result;
            }
            if (i == 3) {
                last = result;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }

        }

        Assert.assertTrue(!first && second && third && !last);
    }
}
