package com.github.cookzhang.ais;

import java.io.Serializable;
import java.sql.ResultSet;

/**
 * User: zhangyi
 * Date: 4/12/14
 * Time: 19:29
 * Description:
 */
public interface DatabaseConverter<T extends Serializable> extends Converter<ResultSet, T> {
}
