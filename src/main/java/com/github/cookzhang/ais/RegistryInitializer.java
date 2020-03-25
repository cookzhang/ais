package com.github.cookzhang.ais;

/**
 * User: zhangyi
 * Date: 5/5/14
 * Time: 10:42
 * Description: 当不用spring管理系统时，需要初始化InvokerRegistry
 */
public interface RegistryInitializer {


    /**
     * 初始化InvokerRegistry
     */
    public void initialize();
}
