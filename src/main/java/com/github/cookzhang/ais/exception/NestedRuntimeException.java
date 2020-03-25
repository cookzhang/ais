package com.github.cookzhang.ais.exception;

/**
 * User: zhangyi
 * Date: 4/14/14
 * Time: 9:39
 * Description:
 */
public class NestedRuntimeException extends RuntimeException {

    public NestedRuntimeException() {
        super();
    }

    public NestedRuntimeException(String message) {
        super(message);
    }

    public NestedRuntimeException(Throwable throwable) {
        super(throwable);
    }

    public NestedRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
