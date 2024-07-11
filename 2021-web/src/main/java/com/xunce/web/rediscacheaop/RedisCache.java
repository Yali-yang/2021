package com.xunce.web.rediscacheaop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {
    //     @RedisCache(key = "'" + Constants.REDIS_FINANCE + ":getCompanyInfoWithStoreInfoByUcid:'+#ucId+'-'+#needFinance", description = "H5绑定发票批次获取公司列表", timeUnit = TimeUnit.MINUTES)

    String description();

    /**
     * 缓存key
     */
    String key();

    /**
     * 缓存过期时间
     */
    long expireTime() default 30;

    /**
     * 缓存过期时间单位 默认毫秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 返回值是空时，是否缓存，防止为空时缓存穿透
     * @return boolean
     */
    boolean emptyNeedCache() default true;

}
