package com.github.cookzhang.ais;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * User: zhangyi
 * Date: 3/13/14
 * Time: 10:52
 * Description: 请求核心数据载体
 */
public class Payload implements Key {

    private static final long serialVersionUID = 1L;

    private String protocol;
    private int port;
    private String lob = "default"; //业务线
    private String charset = "utf-8";                   //返回内容编码
    private String queryString;
    private String queryStringNoParam;
    private int expire = 2 * 60 * 60000;         //2小时
    private int mergeInterval = 0;           //请求合并频率秒
    private int timeout = 10000;                           //请求超时时长，默认10秒
    private int errorTimes = 60;                              //在此时长内统计出错次数，达到阀值自动开启offline. 默认：60秒
    private InvokerType type;                           //控制异步请求时的请求类型
    private Converter converter;
    private transient Supplier<byte[]> supplier;
    private Map<String, String> properties = new HashMap<String, String>();
    private static transient final Logger logger = LoggerFactory.getLogger(Payload.class);

    public Payload() {

    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getQueryStringNoParam() {
        return queryStringNoParam;
    }

    public void setQueryStringNoParam(String queryStringNoParam) {
        this.queryStringNoParam = queryStringNoParam;
    }

    public InvokerType getType() {
        return type;
    }

    public void setType(InvokerType type) {
        this.type = type;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payload)) {
            return false;
        }

        Payload payload = (Payload) o;

        if (port != payload.port) return false;
        if (lob != null ? !lob.equals(payload.lob) : payload.lob != null) return false;
        if (properties != null ? !properties.equals(payload.properties) : payload.properties != null) return false;
        if (protocol != null ? !protocol.equals(payload.protocol) : payload.protocol != null) return false;
        if (queryString != null ? !queryString.equals(payload.queryString) : payload.queryString != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = protocol != null ? protocol.hashCode() : 0;
        result = 31 * result + port;
        result = 31 * result + (lob != null ? lob.hashCode() : 0);
        result = 31 * result + (queryString != null ? queryString.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }

    @Override
    public int expire() {
        return expire;
    }

    public String generate() {
        return DigestUtils.md5Hex(serialize());
    }

    public byte[] serialize() {
        //if(supplier==null)
        //    initSupplier();
        //return supplier.get();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(baos);
        try {
            hessianOutput.writeObject(this);
        } catch (IOException e) {
            logger.error("payload serialize error: {}", e);
        }

        return baos.toByteArray();
    }

    public static Payload deserialize(byte[] data) {
        ByteArrayInputStream bain = new ByteArrayInputStream(data);
        HessianInput hessianInput = new HessianInput(bain);
        try {
            return (Payload) hessianInput.readObject();
        } catch (IOException e) {
            logger.error("payload deserialize error: {}", e);
        }

        return null;
    }

    public void setExpire(int expire) {
        if (expire <= 0) {
            return;
        }

        this.expire = expire;
    }

    public void setMergeInterval(int mergeInterval) {
        this.mergeInterval = mergeInterval;
    }

    public int getMergeInterval() {
        return mergeInterval;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getErrorTimes() {
        return errorTimes;
    }

    public void setErrorTimes(int errorTimes) {
        this.errorTimes = errorTimes;
    }

    public void initSupplier(){
        final Payload payload = this;
        supplier = Suppliers.memoizeWithExpiration(new Supplier<byte[]>() {
            @Override
            public byte[] get() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                HessianOutput hessianOutput = new HessianOutput(baos);
                try {
                    hessianOutput.writeObject(payload);
                } catch (IOException e) {
                    logger.error("payload serialize error: {}", e);
                }

                return baos.toByteArray();
            }
        }, 300, TimeUnit.SECONDS);
    }
}
