package com.xunce.common.completionservicedemo;

import java.util.concurrent.Callable;

public class BaseTask implements Callable<BaseRspDTO<Object>> {
    private String type;
    private IBaseTask iBaseTask;

    public BaseTask(String type, IBaseTask iBaseTask) {
        this.type = type;
        this.iBaseTask = iBaseTask;
    }

    @Override
    public BaseRspDTO<Object> call() throws Exception {
        // 注释的写法，违背了开闭原则，应该对修改关闭，对扩展开放
        // 采用策略模式，通过构造器组合进IBaseTask接口，如果需要扩展，不需要做代码的改动，新增IBaseTask的实现类即可
//        if("userInfo".equals(type)){
//            return new BaseRspDTO("userInfo", "userInfo");
//        }else if("banner".equals(type)){
//            return new BaseRspDTO("banner", "banner");
//        }

        return iBaseTask.executeTask();
    }

}
