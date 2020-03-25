package com.github.cookzhang.ais.exception;

/**
 * User: zhangyi
 * Date: 3/13/14
 * Time: 11:02
 * Description: 核心包异常基类
 */
public class BasicException extends Exception {

    public BasicException() {
        super();
    }

    public BasicException(String message) {
        super(message);
    }

    public BasicException(Throwable throwable) {
        super(throwable);
    }

    public BasicException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
