package com.xunce.common.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class GuavaCacheUtils {

    public static Cache<String, String> nameCache = CacheBuilder
            .newBuilder()
            .maximumSize(2)
            .refreshAfterWrite(1, TimeUnit.DAYS)
            .build();

    /**
     * 设置缓存值
     * 若已有该key值，则会先移除(会触发removalListener移除监听器)，再添加
     */
    public static void put(String key, String value) {
        if (key == null || value == null) {
            return;
        }
        try {
            nameCache.put(key, value);
        } catch (Exception e) {
            log.info("设置缓存值出错", e);
        }
    }

    /**
     * 获取缓存值
     * 如果键不存在值，将调用CacheLoader的load方法，将load方法的返回值加载到缓存
     */
    public static String get(String key) {
        String token = null;
        try {
            token = nameCache.getIfPresent(key);
        } catch (Exception e) {
            log.info("获取缓存值出错", e);
        }
        return token;
    }
}