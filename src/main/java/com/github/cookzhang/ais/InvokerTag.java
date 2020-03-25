package com.github.cookzhang.ais;

import java.lang.annotation.*;

/**
 * User: zhangyi
 * Date: 5/5/14
 * Time: 9:39
 * Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InvokerTag {
    InvokerType type();
}
