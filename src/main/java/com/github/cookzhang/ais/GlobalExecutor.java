package com.github.cookzhang.ais;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: zhangyi
 * Date: 5/20/14
 * Time: 17:34
 * Description:
 */
public class GlobalExecutor {

    private static ExecutorService front;
    private static ExecutorService background;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.SYSTEM_CONFIG_FILE);
        int frontSize = Integer.parseInt(bundle.getString(Constants.GLOBAL_FRONT_THREAD_POOL_SIZE));
        int backgroundSize = Integer.parseInt(bundle.getString(Constants.GLOBAL_BACKGROUND_THREAD_POOL_SIZE));
        front = Executors.newFixedThreadPool(frontSize);
        background = Executors.newFixedThreadPool(backgroundSize);
    }

    /**
     * 用户端直接调用的线程池，保证用户的及时响应，可以适当调大
     *
     * @return 线程池
     */
    public static ExecutorService getFront() {
        return front;
    }

    /**
     * 程序后台调用的线程池，在线程紧张的情况下，可以适当调小
     *
     * @return 线程池
     */
    public static ExecutorService getBackground() {
        return background;
    }
}
