package com.xunce.web.redislock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author menglin
 * @date 2024/06/07
 * <p>
 * @RedisLock(key = "'" + RedisPrefixConstant.BPM_COMPLETE_TASK + "'+#id", expireTime = 3000, errorMessage = "重复提交，请稍后。")
 * </p>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {


    String key();

    /**
     * 获取锁模式
     *
     * @return
     */
    RedisLockMode lockMode() default RedisLockMode.FAST_FAIL;

    /**
     * 获取锁等待时间
     *
     * @return
     */
    long waitTime() default -1;

    /**
     * 超时时间
     * redisson会自动续期，一直存在，必须手动释放
     * 只有设置成负数才会自动续期
     * @return
     */
    long expireTime() default -1;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 加锁失败message文案
     *
     * @return message
     */
    String errorMessage() default "正在处理中，请稍后再试";
}
