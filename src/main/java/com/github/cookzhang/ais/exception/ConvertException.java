package com.github.cookzhang.ais.exception;

/**
 * User: zhangyi
 * Date: 4/14/14
 * Time: 9:41
 * Description:
 */
public class ConvertException extends NestedRuntimeException {

    public ConvertException() {
        super();
    }

    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(Throwable throwable) {
        super(throwable);
    }

    public ConvertException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
