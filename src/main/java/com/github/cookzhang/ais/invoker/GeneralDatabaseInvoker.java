package com.github.cookzhang.ais.invoker;

import com.autohome.ais.*;
import com.github.cookzhang.ais.exception.ConvertException;
import com.github.cookzhang.ais.exception.InvokeException;
import com.github.cookzhang.ais.util.ResultSetUtils;
import com.github.cookzhang.ais.Constants;
import com.github.cookzhang.ais.Converter;
import com.github.cookzhang.ais.DatabaseConverter;
import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.InvokerTag;
import com.github.cookzhang.ais.InvokerType;
import com.github.cookzhang.ais.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * User: zhangyi
 * Date: 4/9/14
 * Time: 21:51
 * Description:
 */
@InvokerTag(type = InvokerType.DATABASE)
public class GeneralDatabaseInvoker<V extends Serializable> implements Invoker<V> {

    private static final Logger logger = LoggerFactory.getLogger(GeneralDatabaseInvoker.class);
    private ResourceBundle resourceBundle = ResourceBundle.getBundle(Constants.JDBC_CONFIG_FILE);

    public Connection getConnection() throws Exception {
        Class.forName(resourceBundle.getString(Constants.DB_DRIVERCLASSNAME));
        String url = resourceBundle.getString(Constants.DB_URL);
        String username = resourceBundle.getString(Constants.DB_USERNAME);
        String password = resourceBundle.getString(Constants.DB_PASSWORD);
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * 执行请求
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
    public <S> V invoke(Payload payload, Converter<S, V> converter) throws InvokeException {
        try {
            Statement statement = getConnection().createStatement();
            logger.debug("the final query String is :{}", payload.getQueryString());
            logger.info("general database invoke starting......");
            ResultSet resultSet = statement.executeQuery(payload.getQueryString());
            logger.info("general database invoke succeed.");

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
        } catch (Exception e) {
            logger.error("sql query error: {}", e);
            throw new InvokeException(e);
        }
    }
}
