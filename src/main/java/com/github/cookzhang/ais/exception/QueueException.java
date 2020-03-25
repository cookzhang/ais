package com.github.cookzhang.ais.exception;

/**
 * User: zhangyi
 * Date: 4/9/14
 * Time: 13:49
 * Description:
 */
public class QueueException extends BasicException {
    public QueueException() {
        super();
    }

    public QueueException(String message) {
        super(message);
    }

    public QueueException(Throwable throwable) {
        super(throwable);
    }

    public QueueException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
