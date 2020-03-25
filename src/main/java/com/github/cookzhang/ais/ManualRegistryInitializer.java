package com.github.cookzhang.ais;

import com.github.cookzhang.ais.invoker.GeneralDatabaseInvoker;
import com.github.cookzhang.ais.invoker.HttpInvoker;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * User: zhangyi
 * Date: 5/5/14
 * Time: 10:31
 * Description:
 */
public class ManualRegistryInitializer implements RegistryInitializer {

    /**
     * 初始化InvokerRegistry
     */
    @Override
    public void initialize() {
        InvokerRegistry registry = InvokerRegistry.getInstance();
        Map<InvokerType, Invoker<Serializable>> map = Maps.newHashMap();
        map.put(InvokerType.DATABASE, new GeneralDatabaseInvoker<Serializable>());
        map.put(InvokerType.HTTP, new HttpInvoker<Serializable>());
        registry.setInvokers(map);
    }
}
