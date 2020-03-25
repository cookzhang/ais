package com.github.cookzhang.ais.invoker;

import com.autohome.ais.*;
import com.github.cookzhang.ais.exception.ConvertException;
import com.github.cookzhang.ais.exception.InvokeException;
import com.github.cookzhang.ais.log.AisLog;
import com.github.cookzhang.ais.Converter;
import com.github.cookzhang.ais.HttpConverter;
import com.github.cookzhang.ais.Invoker;
import com.github.cookzhang.ais.InvokerTag;
import com.github.cookzhang.ais.InvokerType;
import com.github.cookzhang.ais.Payload;
import com.google.common.base.Stopwatch;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * User: zhangyi
 * Date: 3/13/14
 * Time: 11:25
 * Description: http协议请求器
 */
@InvokerTag(type = InvokerType.HTTP)
public class HttpInvoker<V extends Serializable> implements Invoker<V> {

    private static final Logger logger = LoggerFactory.getLogger(HttpInvoker.class);

    /**
     * @param payload 业务负载数据
     * @return 请求结果
     * @throws InvokeException 当请求失败、或者请求数据结构错误、或请求数据显示服务器处理失败时，抛出该异常
     */
    @SuppressWarnings("unchecked")
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
        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("the final query String is :{}", payload.getQueryString());
        String queryString = payload.getQueryString();
        if (queryString == null || queryString.trim().length() == 0) {
            throw new InvokeException("请求queryString为空");
        }

        HttpGet httpGet;
        if (queryString.toLowerCase().startsWith("http://")) {
            httpGet = new HttpGet(payload.getQueryString());
        } else {
            httpGet = new HttpGet("http://" + payload.getQueryString());
        }

        RequestConfig requestConfig = RequestConfig.custom().
                setSocketTimeout(payload.getTimeout()).
                setConnectTimeout(payload.getTimeout()).
                build();//设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        //httpGet.setHeader("Connection", "close");
        Map<String, String> params = payload.getProperties();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }

        HttpResponse response;
        Stopwatch watch  = new Stopwatch();
        watch.start();
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            logger.error("http请求失败:{}", payload);
            logger.error(e.getMessage());
            throw new InvokeException("请求失败!");
        }

        int status = response.getStatusLine().getStatusCode();
        if (status != HttpStatus.SC_OK) {
            response = redirect(httpClient,response, httpGet);

        }
        long nanos = watch.elapsed(TimeUnit.MILLISECONDS);
        watch.stop();
        AisLog.getInstance().appendLog("ApiResponseTime@{}`{}`{}",
                payload.getLob(),
                payload.getQueryStringNoParam(),
                nanos);
        if (converter == null) {
            V v;
            try {
                String result;
                try {
                    result = EntityUtils.toString(response.getEntity(), payload.getCharset());
                } catch (IOException e) {
                    logger.error("get content from response error.");
                    throw new InvokeException("get content from response error.", e);
                }

                v = (V) result;
            } catch (RuntimeException e1) {
                try {
                    byte[] result;
                    try {
                        result = EntityUtils.toByteArray(response.getEntity());
                    } catch (IOException e) {
                        logger.error("get content from response error.");
                        throw new InvokeException("get content from response error.", e);
                    }
                    v = (V) result;
                } catch (RuntimeException e2) {
                    throw new ConvertException("convert error.");
                }
            }
            closehttpclient(httpClient,httpGet);
            return v;
        }

        if (converter instanceof HttpConverter) {
            closehttpclient(httpClient,httpGet);
            HttpConverter<V> dc = (HttpConverter<V>) converter;
            return dc.convert(response);
        }
        closehttpclient(httpClient,httpGet);
        @SuppressWarnings("unchecked")
        S s = (S) response;
        return converter.convert(s);
    }

    /**
     * 支持 3** 跳转功能
     *
     * @param response 上次请求response
     * @param httpGet  上次请求httpGet
     * @return 如果跳转成功，返回跳转之后的response
     * @throws InvokeException
     */
    private HttpResponse redirect(CloseableHttpClient httpClient ,HttpResponse response, HttpGet httpGet) throws InvokeException {

        int statusCode = response.getStatusLine().getStatusCode();
        if ((statusCode == HttpStatus.SC_MOVED_TEMPORARILY) ||
                (statusCode == HttpStatus.SC_MOVED_PERMANENTLY) ||
                (statusCode == HttpStatus.SC_SEE_OTHER) ||
                (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {

            //读取新的URL地址
            Header header = response.getFirstHeader("location");
            if (header != null) {
                String uri = header.getValue();
                if ((uri == null) || (uri.equals(""))) {
                    uri = "/";
                }

                httpGet.setURI(URI.create(uri));
                try {
                    return httpClient.execute(httpGet);
                } catch (IOException e) {
                    throw new InvokeException(e);
                }
            } else {
                throw new InvokeException("Invalid redirect");
            }
        }
        closehttpclient(httpClient,httpGet);
        throw new InvokeException("http invoke return an error status code:{}" + statusCode);
    }
    private void closehttpclient(CloseableHttpClient httpClient,HttpGet httpGet)throws InvokeException{
        try {
            httpGet.releaseConnection();
            httpClient.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InvokeException("请求关闭连接失败!");
        }
    }
}
