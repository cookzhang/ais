package com.github.cookzhang.ais;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * User: zhangyi
 * Date: 3/18/14
 * Time: 19:03
 * Description:
 */
public class InvokerRegistry<V extends Serializable> {

    private EnumMap<InvokerType, Invoker<V>> invokers;

    public static <V extends Serializable> InvokerRegistry<V> getInstance() {
        return InvokerRegistryHolder.instance;
    }

    public void setInvokers(Map<InvokerType, Invoker<V>> invokers) {
        this.invokers = new EnumMap<InvokerType, Invoker<V>>(invokers);
    }

    public Invoker<V> findInvoker(InvokerType type) {
        return invokers.get(type);
    }

    public static class InvokerRegistryHolder {
        private static InvokerRegistry instance = new InvokerRegistry();
    }
}
