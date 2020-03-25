package com.github.cookzhang.ais;

import com.github.cookzhang.ais.exception.ConvertException;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/12/14
 * Time: 17:29
 * Description: 请求最终结果数据格式转换器
 */
public interface Converter<S, T extends Serializable> extends Serializable {

    /**
     * 从E这种数据类型转换成T类型
     *
     * @param s 源型数据
     * @return 转换后的类型
     */
    public T convert(S s) throws ConvertException;
}
