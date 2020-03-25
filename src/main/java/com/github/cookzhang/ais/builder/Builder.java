package com.github.cookzhang.ais.builder;

import com.github.cookzhang.ais.Invoker;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/7/14
 * Time: 11:52
 * Description:
 */
public interface Builder<T extends Serializable> {

    /**
     * 生成Invoker中需要的marshaller
     */
    public void makeMarshaller();

    /**
     * 生成Invoker中需要的messageProducer
     */
    public void makeMessageProducer();

    /**
     * 生成异步请求时请求合并的判断器
     */
    public void makePredicate();

    /**
     * 动态Invoker生成方法
     *
     * @return 动态invoker
     */
    public Invoker<T> createDynamic();

    /**
     * 静态Invoker生成方法
     *
     * @return 静态invoker
     */
    public Invoker<T> createStatic();
}
