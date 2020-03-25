package com.github.cookzhang.ais.convert;

import com.github.cookzhang.ais.Converter;
import com.github.cookzhang.ais.exception.ConvertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * User: zhangyi
 * Date: 4/14/14
 * Time: 9:28
 * Description: bytes 转换成 string的转换类
 */
public class ByteToStringConverter implements Converter<byte[], String> {

    private static final Logger logger = LoggerFactory.getLogger(ByteToStringConverter.class);

    public String convert(byte[] bytes) {

        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("convert from byte to string error: {}", e);
            throw new ConvertException("convert from byte to string error: {}", e);
        }
    }
}
