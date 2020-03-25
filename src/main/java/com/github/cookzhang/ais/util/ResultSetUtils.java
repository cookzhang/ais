package com.github.cookzhang.ais.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Map;

/**
 * User: zhangyi
 * Date: 4/9/14
 * Time: 19:54
 * Description:
 */
public class ResultSetUtils {
    public static String toJson(ResultSet resultSet) {
        List<Map<String, String>> list = Lists.newArrayList();
        try {
            // 获取列数
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 遍历ResultSet中的每条数据
            while (resultSet.next()) {
                Map<String, String> meta = Maps.newHashMap();

                // 遍历每一列
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    String value = resultSet.getString(columnName);
                    meta.put(columnName, value);
                }
                list.add(meta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        return gson.toJson(list);
    }

    public static byte[] toBytes(ResultSet resultSet, String charset) throws UnsupportedEncodingException {
        List<Map<String, String>> list = Lists.newArrayList();
        try {
            // 获取列数
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 遍历ResultSet中的每条数据
            while (resultSet.next()) {
                Map<String, String> meta = Maps.newHashMap();

                // 遍历每一列
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    String value = resultSet.getString(columnName);
                    meta.put(columnName, value);
                }
                list.add(meta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String result = gson.toJson(list);
        return result.getBytes(charset);
    }

    public static byte[] toBytes(ResultSet resultSet) {
        List<Map<String, String>> list = Lists.newArrayList();
        try {
            // 获取列数
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 遍历ResultSet中的每条数据
            while (resultSet.next()) {
                Map<String, String> meta = Maps.newHashMap();

                // 遍历每一列
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    String value = resultSet.getString(columnName);
                    meta.put(columnName, value);
                }
                list.add(meta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String result = gson.toJson(list);
        return result.getBytes();
    }
}
