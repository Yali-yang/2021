package com.xunce.web.redislock.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author menglin
 * redisson联锁，将多个锁结合起来，当所有的锁都被成功获取时，才算成功获取到锁
 * <p>
 * @RedisLock(keys = {"'" + RedisPrefixConstant.BPM_COMPLETE_TASK + "'+#id"}, expireTime = 3000, errorMessage = "重复提交，请稍后。")
 * </p>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisMultiLock {
    String[] keys();

    RedisLockMode lockMode() default RedisLockMode.FAST_FAIL;

    long waitTime() default -1;

    long expireTime() default 2000;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 加锁失败message文案
     *
     * @return message
     */
    String errorMessage() default "重复提交，请稍后";
}
