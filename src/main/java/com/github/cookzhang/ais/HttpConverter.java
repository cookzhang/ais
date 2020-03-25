package com.github.cookzhang.ais;

import org.apache.http.HttpResponse;

import java.io.Serializable;

/**
 * User: zhangyi
 * Date: 4/12/14
 * Time: 19:29
 * Description:
 */
public interface HttpConverter<T extends Serializable> extends Converter<HttpResponse, T> {
}
