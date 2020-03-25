package com.github.cookzhang.ais;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/23/14
 * Time: 19:22
 * Description: 数据类型转换器工具类
 */
public class Converters {

    public static <E, V extends Serializable> Converter<E, V> nullConverter() {
        return new NullConverter<E, V>();
    }

    private static class NullConverter<E, V extends Serializable> implements Converter<E, V> {
        /**
         * 从E这种数据类型转换成T类型
         *
         * @param e 源型数据
         * @return 转换后的类型
         */
        @Override
        public V convert(E e) {
            return null;
        }
    }

    private static class EmptyConverter<E, V extends Serializable> implements Converter<E, V> {
        /**
         * 从E这种数据类型转换成T类型
         *
         * @param e 源型数据
         * @return 转换后的类型
         */
        @Override
        public V convert(E e) {
            @SuppressWarnings("unchecked")
            V v = (V) e;
            return v;
        }
    }
}
