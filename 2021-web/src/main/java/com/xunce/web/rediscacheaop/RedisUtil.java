package com.xunce.web.rediscacheaop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisUtil {

    @Resource
    private RedissonClient redissonClient;


    public Object getObject(String cacheKey, Class returnClass){
        try {
            if(StringUtils.isBlank(cacheKey)){
                return null;
            }
            Object data =  redissonClient.getBucket(cacheKey).get();
            if (Objects.isNull(data)){
                return null;
            }
            return JSONObject.parseObject(cacheKey, returnClass);
        }catch (Exception e){
            log.error("redis getObject error", e);
        }

        return null;
    }

    public Boolean setObject(String cacheKey, Object obj, Long timeout, TimeUnit timeUnit){
        if(StringUtils.isBlank(cacheKey) || Objects.isNull(obj)){
            return Boolean.FALSE;
        }
        try {
            redissonClient.getBucket(cacheKey).set(JSON.toJSONString(obj), timeout, timeUnit);
            return Boolean.TRUE;
        }catch (Exception e){
            log.error("redis setObject error", e);
        }

        return Boolean.FALSE;

    }

    public Boolean isExists(String cacheKey){
        boolean exists = redissonClient.getBucket(cacheKey).isExists();
        return exists;
    }

    public Boolean deleteKey(String cacheKey){
        return redissonClient.getBucket(cacheKey).delete();
    }

}
