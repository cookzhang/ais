package com.github.cookzhang.ais;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/9/14
 * Time: 13:24
 * Description:
 */
public interface Key extends Serializable {

    public int expire();

    public String generate();
}
