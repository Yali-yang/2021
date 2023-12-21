package com.xunce.common.designpattern.strategy.postconstruct;

import javax.annotation.PostConstruct;

public abstract class AsyncBaseHandler {



    /**
     * 异步处理器名称
     * @return 处理器名称
     * @see
     */
    public abstract String getHandlerName();

    /**
     * 处理器注册函数,默认再服务启动时,把相关的处理器都注册到工厂中
     */
    @PostConstruct  // 这个是jdk自带的注解
    public void register() {
        AsyncHandlerFactory.registerHandler(this);
    }

    /**
     * 处理器处理方法
     * @param param
     */
    public abstract void handle(String param);


}
