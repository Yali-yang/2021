package com.xunce.web.exception;

/**
 * RedisLockException
 * @author zhaozonglu
 */
public class RedisBusinessException extends RuntimeException {
    public RedisBusinessException() {
        super();
    }

    public RedisBusinessException(String message) {
        super(message);
    }

    public RedisBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisBusinessException(Throwable cause) {
        super(cause);
    }
}
