package com.github.cookzhang.ais;

import com.github.cookzhang.ais.util.Scanner;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * User: zhangyi
 * Date: 5/5/14
 * Time: 10:37
 * Description:
 */
public class ScanRegistryInitializer implements RegistryInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ScanRegistryInitializer.class);

    /**
     * 初始化InvokerRegistry
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initialize() {
        InvokerRegistry registry = InvokerRegistry.getInstance();
        Map<InvokerType, Invoker<Serializable>> map = Maps.newHashMap();
        List<String> files = Scanner.getClassName("com.autohome.ais");
        try {
            for (String file : files) {
                Class<?> clazz = Class.forName(file);
                if (!Invoker.class.isAssignableFrom(clazz) || !clazz.isAnnotationPresent(InvokerTag.class)) {
                    continue;
                }
                Annotation annotation = clazz.getAnnotation(InvokerTag.class);
                Method m = annotation.getClass().getDeclaredMethod("type", null);
                InvokerType type = (InvokerType) m.invoke(annotation, null);
                Constructor<?>[] cs = clazz.getConstructors();
                Constructor<Invoker<Serializable>> constructor = (Constructor<Invoker<Serializable>>) cs[0];
                constructor.newInstance();

                map.put(type, constructor.newInstance());
            }

            registry.setInvokers(map);
        } catch (Exception e) {
            logger.error("initialize invoker error:{}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        ScanRegistryInitializer application = new ScanRegistryInitializer();
        application.initialize();
        InvokerRegistry registry = InvokerRegistry.getInstance();
        System.out.println(registry.findInvoker(InvokerType.DATABASE));
    }
}
