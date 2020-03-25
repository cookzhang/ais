package com.github.cookzhang.ais.exception;

/**
 * User: zhangyi
 * Date: 3/13/14
 * Time: 11:02
 * Description: 请求异常类
 */
public class InvokeException extends BasicException {

    public InvokeException() {
        super();
    }

    public InvokeException(String message) {
        super(message);
    }

    public InvokeException(Throwable throwable) {
        super(throwable);
    }

    public InvokeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
