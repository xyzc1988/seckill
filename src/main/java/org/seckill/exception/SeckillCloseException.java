package org.seckill.exception;

/**
 * 秒杀关闭异常
 * Created by zhangcheng on 2016/6/19.
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
