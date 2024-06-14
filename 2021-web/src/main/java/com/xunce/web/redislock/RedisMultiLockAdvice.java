package com.xunce.web.redislock;

import com.xunce.web.exception.RedisBusinessException;
import com.xunce.web.redislock.annotation.RedisMultiLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhaozonglu001
 * @date 2023/6/17
 */
@Order(1)
@Aspect
@Component
@Slf4j
public class RedisMultiLockAdvice {

    @Resource
    private RedissonClient defaultRedissonClient;

    @Resource
    CommonExpressionParser commonExpressionParser;

    @Around("within(com.ke.lease.home.zulin.caiwu..*) && @annotation(redismultiLock)")
    public Object process(ProceedingJoinPoint pjp, RedisMultiLock redismultiLock) throws Throwable {
        RLock[] rLocks = new RLock[redismultiLock.keys().length];
        RedissonMultiLock multiLock = null;

        boolean isLocked = false;
        List<String> realKeysName = new ArrayList<>();
        try {
            log.info("lock info:{}", redismultiLock);

            for (int i = 0; i < redismultiLock.keys().length; i++) {
                RLock lock = buildRLock(pjp, redismultiLock.keys(), i, realKeysName);
                rLocks[i] = lock;
            }
            multiLock = new RedissonMultiLock(rLocks);
            isLocked = doLock(redismultiLock, multiLock, realKeysName);
            return pjp.proceed();
        } finally {
            processUnlock(isLocked, multiLock, realKeysName);
        }

    }

    private RLock buildRLock(ProceedingJoinPoint pjp, String[] rawKeys, int index, List<String> realKeysName) {
        String realKey = commonExpressionParser.getRealKey(rawKeys[index], pjp);
        rawKeys[index] = realKey;
        RLock lock = defaultRedissonClient.getLock(realKey);
        realKeysName.add(realKey);
        return lock;
    }

    private boolean doLock(RedisMultiLock redismultiLock, RLock lock, List<String> realKeys) throws InterruptedException {
        boolean isLocked = false;
        switch (redismultiLock.lockMode()) {
            case FAST_FAIL:
                isLocked = lock.tryLock(-1, redismultiLock.expireTime(), redismultiLock.timeUnit());
                break;
            case TRY_WAIT:
                if (redismultiLock.waitTime() < 0) {
                    throw new RedisBusinessException("redis lock wait mode wait time must larger 0");
                }
                isLocked = lock.tryLock(redismultiLock.waitTime(), redismultiLock.expireTime(), redismultiLock.timeUnit());
                break;
            case WAIT:
                lock.lock(redismultiLock.expireTime(), redismultiLock.timeUnit());
                isLocked = true;
            default:
                break;
        }
        if (!isLocked) {
            log.warn("can not get lock, lock mode:{}, keys:{} thread:{}", redismultiLock.lockMode(), realKeys, Thread.currentThread().getName());
            throw new RedisBusinessException(redismultiLock.errorMessage());
        } else {
            log.info("get locks, keys:{}", realKeys);
        }
        return true;
    }

    private void processUnlock(boolean isLocked, RedissonMultiLock lock, List<String> realKeys) {
        try {
            if (Objects.nonNull(lock) && isLocked) {
                log.info("try release locks, keys:{}", realKeys);
                lock.unlock();
            }
        } catch (IllegalMonitorStateException imse) {
            // 锁超时被释放场景时，再去调unlock方法，会报IllegalMonitorStateException异常
            log.warn("release lock warn IllegalMonitorStateException.ex:{}", imse.getMessage());
        } catch (Exception e) {
            //释放锁的时候error日志提醒,不作为卡点
            log.error("release lock error,keys:{}, reason:{}", realKeys, e.getMessage(), e);
        }
    }
}
