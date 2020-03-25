package com.github.cookzhang.ais.invoker;

import com.github.cookzhang.ais.Converter;
import com.github.cookzhang.ais.DatabaseConverter;
import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.exception.ConvertException;
import com.github.cookzhang.ais.exception.InvokeException;
import com.github.cookzhang.ais.util.ResultSetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;

/**
 * User: zhangyi
 * Date: 4/1/14
 * Time: 20:31
 * Description:
 */
public class SpringDatabaseInvoker<V extends Serializable> implements Invoker<V> {

    private static final Logger logger = LoggerFactory.getLogger(SpringDatabaseInvoker.class);
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 不建议在做数据库和http调用时使用该方法，建议使用结果显示转换方法 {@link SpringDatabaseInvoker#invoke(Payload, Converter)} 有利于提高转换性能、降低出错机率
     *
     * @param payload 业务负载数据
     * @return 返回请求结果
     */
    @Override
    public V invoke(Payload payload) throws InvokeException {
        return invoke(payload, null);
    }

    /**
     * 执行请求
     *
     * @param payload   业务负载数据
     * @param converter 数据类型转换器
     * @return 返回请求结果
     */
    @Override
    @SuppressWarnings("unchecked")
    public <S> V invoke(Payload payload, final Converter<S, V> converter) throws InvokeException {
        logger.debug("the final query String is :{}", payload.getQueryString());
        logger.info("spring database invoke starting......");

        ResultSet resultSet;
        try {
            ResultSetWrappingSqlRowSet rowSet = (ResultSetWrappingSqlRowSet) jdbcTemplate.queryForRowSet(payload.getQueryString());
            resultSet = rowSet.getResultSet();
        } catch (Exception e) {
            logger.error("spring database invoke error:{}", e.getMessage());
            throw new InvokeException("spring database invoke error", e);
        }

        /**
         * 不建议在做数据库和http调用时使用 invoker.invoke(payload) 方法，
         * 使用结果显示转换方法 invoker.invoke(payload, converter) 有利于提高转换性能、降低出错机率
         */
        if (converter == null) {
            V v;
            String result = null;
            try {
                result = ResultSetUtils.toJson(resultSet);
                v = (V) result;
            } catch (RuntimeException e1) {
                try {
                    v = (V) result.getBytes(payload.getCharset());
                } catch (UnsupportedEncodingException e2) {
                    throw new ConvertException("convert error.");
                }
            }

            return v;
        }

        if (converter instanceof DatabaseConverter) {
            DatabaseConverter<V> dc = (DatabaseConverter<V>) converter;
            return dc.convert(resultSet);
        }

        @SuppressWarnings("unchecked")
        S s = (S) resultSet;
        return converter.convert(s);
    }
}
