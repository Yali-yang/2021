package com.xunce.web.rediscacheaop;

import com.alibaba.fastjson.JSONObject;
import com.xunce.web.exception.RedisBusinessException;
import com.xunce.web.redislock.CommonExpressionParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author zhaozonglu001
 * @date 2023/6/19
 */
@Order(1)
@Aspect
@Component
@Slf4j
public class RedisCacheAdvice {
    public static final String REDIS_DATA_EMPTY = "emptyRedisValueForNull";
    @Resource
    private RedissonClient defaultRedissonClient;
    @Resource
    private CommonExpressionParser commonExpressionParser;

    @Around("@annotation(redisCache)")
    public Object process(ProceedingJoinPoint pjp, RedisCache redisCache) throws Throwable {
        long startTime = System.currentTimeMillis();
        String cacheKey = redisCache.key();
        String realKey = commonExpressionParser.getRealKey(cacheKey, pjp);
        try {
            Object o = defaultRedissonClient.getBucket(realKey).get();
            if (Objects.nonNull(o)) {
                // json格式序列化
                String resultStr = (String) o;
                if (REDIS_DATA_EMPTY.equals(resultStr)) {
                    // redis值为null的默认值
                    if (redisCache.emptyNeedCache()) {
                        return null;
                    } else {
                        throw new RedisBusinessException("redis缓存为空值");
                    }
                }
                // 获取调用方法的返回值
                Class<?> clazz = ((MethodSignature) pjp.getSignature()).getReturnType();
                if (clazz == String.class) {
                    return resultStr;
                }
                Object obj = JSONObject.parseObject(resultStr, getReturnType(pjp));
                log.info(redisCache.description() + "成功命中缓存，redisKey=" + realKey
                        + ";用时" + (System.currentTimeMillis() - startTime) + "ms");
                return obj;
            }
        } catch (Exception e) {
            log.warn("从redis中获取缓存数据失败，将发起数据请求。redisKey:{} message:{}", realKey, e.getMessage());
        }
        // 调用业务处理方法
        Object object = pjp.proceed();
        try {
            // 异常只打印日志，正常进行服务请求
            String redisValue;
            if (Objects.isNull(object)) {
                if (redisCache.emptyNeedCache()) {
                    redisValue = REDIS_DATA_EMPTY;
                } else {
                    return null;
                }
            } else if (object instanceof String) {
                // 如果是string 直接放入缓存
                redisValue = (String) object;
            } else {
                // 将返回结果存入缓存，对象转为json
                redisValue = JSONObject.toJSONString(object);
            }
            defaultRedissonClient.getBucket(realKey).set(redisValue, redisCache.expireTime(), redisCache.timeUnit());
            log.info(redisCache.description() + "成功创建缓存，redisKey=" + realKey + ";用时" + (System.currentTimeMillis() - startTime) + "ms");
        } catch (Exception e) {
            log.error("从redis中创建缓存数据失败redisKey:{}", realKey);
        }
        return object;
    }

    private Type getReturnType(ProceedingJoinPoint joinPoint) {
        Method targetMethod = null;
        String methodName = joinPoint.getSignature().getName();
        Method[] methodArr = joinPoint.getSignature().getDeclaringType().getMethods();
        for (Method method : methodArr) {
            if (method.getName().equals(methodName)) {
                targetMethod = method;
                break;
            }
        }
        if (targetMethod == null) {
            throw new RuntimeException("遍历AOP类，未获取到指定名称的方法数据");
        }
        return targetMethod.getGenericReturnType();
    }

}