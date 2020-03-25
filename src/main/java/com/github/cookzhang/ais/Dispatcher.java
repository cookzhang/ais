package com.github.cookzhang.ais;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/4/14
 * Time: 14:10
 * Description: 任务高度器接口
 */
public interface Dispatcher {

    /**
     * 调度方法
     *
     * @param payload 请求数据负载
     */
    public Serializable submit(byte[] payload);
}
