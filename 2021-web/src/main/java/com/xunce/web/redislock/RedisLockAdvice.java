package com.xunce.web.redislock;

import com.xunce.web.exception.RedisBusinessException;
import com.xunce.web.redislock.annotation.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author menglin
 * @date 2024/06/07
 */
@Order(1)
@Aspect
@Component
@Slf4j
public class RedisLockAdvice {

    @Resource
    private RedissonClient defaultRedissonClient;

    @Resource
    private CommonExpressionParser commonExpressionParser;

    @Around("within(com.xunce.web..*) && @annotation(redisLock)")
    public Object process(ProceedingJoinPoint pjp, RedisLock redisLock) throws Throwable {

        RLock lock = null;
        boolean isLocked = false;
        String realKey = null;
        try {
            log.info("lock info:{}", redisLock);
            realKey = commonExpressionParser.getRealKey(redisLock.key(), pjp);
            lock = defaultRedissonClient.getLock(realKey);
            isLocked = doLock(redisLock, lock, realKey);
            return pjp.proceed();
        } finally {
            unlock(isLocked, lock, realKey);
        }
    }

    private boolean doLock(RedisLock redisLock, RLock lock, String realKey) throws InterruptedException {
        boolean isLocked = false;
        switch (redisLock.lockMode()) {
            case FAST_FAIL:
                isLocked = lock.tryLock(-1, redisLock.expireTime(), redisLock.timeUnit());
                break;
            case TRY_WAIT:
                if (redisLock.waitTime() < 0) {
                    throw new RedisBusinessException("redis lock wait mode wait time must larger 0");
                }
                isLocked = lock.tryLock(redisLock.waitTime(), redisLock.expireTime(), redisLock.timeUnit());
                break;
            case WAIT:
                lock.lock(redisLock.expireTime(), redisLock.timeUnit());
                isLocked = true;
            default:
                break;
        }
        if (!isLocked) {
            log.warn("get lock failed, lock mode:{}, key:{} thread:{}", redisLock.lockMode(), realKey, Thread.currentThread().getName());
            throw new RedisBusinessException(redisLock.errorMessage());
        } else {
            log.info("get lock success, key:{}, thread:{}", realKey, Thread.currentThread().getName());
        }
        return true;
    }

    private void unlock(boolean isLocked, RLock lock, String realKey) {
        try {
            if (Objects.nonNull(lock) && isLocked && lock.isHeldByCurrentThread()) {
                log.info("try release locks, key:{}", realKey);
                lock.unlock();
            }
        } catch (IllegalMonitorStateException imse) {
            // 锁超时被释放场景时，再去调unlock方法，会报IllegalMonitorStateException异常
            log.warn("release lock warn IllegalMonitorStateException.ex:{}", imse.getMessage());
        } catch (Exception e) {
            //释放锁的时候error日志提醒,不作为卡点
            log.error("release lock error,key:{}, reason:{}", realKey, e.getMessage(), e);
        }
    }
}
