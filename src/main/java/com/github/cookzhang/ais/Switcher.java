package com.github.cookzhang.ais;

/**
 * User: zhangyi
 * Date: 3/21/14
 * Time: 17:56
 * Description: offline策略接口
 */
public interface Switcher {

    public void setInvoker(Invoker invoker);

    /**
     * 是否开启offline 功能
     *
     * @param payload 业务请求数据负载
     * @return 是否开启了offline功能
     */
    public boolean isOpen(Payload payload);

    /**
     * 关闭offline功能
     *
     * @param payload 请求数据负载
     */
    public void close(Payload payload);

    /**
     * 当发生源请求错误时，激发该方法
     *
     * @param payload 业务请求负载
     */
    public void trigger(Payload payload);
}
