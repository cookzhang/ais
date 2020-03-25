package com.github.cookzhang.ais;

import com.github.cookzhang.ais.exception.InvokeException;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 3/13/14
 * Time: 10:57
 * Description: 请求执行器
 */
public interface Invoker<T extends Serializable> {

    /**
     * 执行请求
     *
     * @param payload 业务负载数据
     * @return 返回请求结果
     */
    public T invoke(final Payload payload) throws InvokeException;

    /**
     * 执行请求
     *
     * @param payload   业务负载数据
     * @param converter 数据类型转换器
     * @return 返回请求结果
     */
    public <S> T invoke(Payload payload, Converter<S, T> converter) throws InvokeException;
}
