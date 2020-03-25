package com.github.cookzhang.ais.convert;

import com.github.cookzhang.ais.Converter;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;

/**
 * User: zhangyi
 * Date: 4/14/14
 * Time: 9:21
 * Description: 转换ResultSet为json 字符串
 */
public class ResultSetToStringConverter implements Converter<ResultSet, String> {

    private static final Logger logger = LoggerFactory.getLogger(ResultSetToStringConverter.class);

    /**
     * 从ResultSet这种数据类型转换成 json String 类型
     *
     * @param resultSet 源型数据
     * @return 转换后的 String 类型
     */
    @Override
      public String convert(ResultSet resultSet) {
        Gson gson = new Gson();
        String gsonStr = gson.toJson(resultSet);
        logger.debug("resultSet gson string is:{}",gsonStr);
        return gsonStr;
    }
}
