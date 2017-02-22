package org.seckill.exception;

/**
 * Created by zhangcheng on 2016/6/19.
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
