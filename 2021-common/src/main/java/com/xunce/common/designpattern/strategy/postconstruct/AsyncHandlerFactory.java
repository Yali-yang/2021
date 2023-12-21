package com.xunce.common.designpattern.strategy.postconstruct;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 入账异步处理类
 * @author
 */
@Slf4j
public class AsyncHandlerFactory {

    /**
     * 存储所有的处理器名称和处理器的映射关系
     */
    private static final ConcurrentHashMap<String, AsyncBaseHandler> HANDLER_MAP = new ConcurrentHashMap();

    /**
     * 注册处理器
     * @param handler 处理器
     */
    public static void registerHandler(AsyncBaseHandler handler) {
        if (Objects.isNull(handler)) {
            log.error("注册异步处理器为空");
            return;
        }
        HANDLER_MAP.put(handler.getHandlerName(), handler);
    }

    /**
     * 处理器处理流程
     * @param handlerName 处理器名称
     * @param data 处理器依赖的数据
     */
    public static void handle(String handlerName, String data) {
        //通过处理器的名称获取异步处理器
        AsyncBaseHandler handler = HANDLER_MAP.get(handlerName);
        //如果处理器为空,直接返回
        if (Objects.isNull(handler)) {
            log.error("获取异步处理器为空, name:{}", handlerName);
            return;
        }
        handler.handle(data);
    }

}

