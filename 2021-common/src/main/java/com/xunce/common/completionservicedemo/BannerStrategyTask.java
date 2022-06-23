package com.xunce.common.completionservicedemo;

public class BannerStrategyTask implements IBaseTask{

    @Override
    public String getType() {
        return "Banner";
    }

    @Override
    public BaseRspDTO<Object> executeTask() {
        return new BaseRspDTO<>(getType(), "Banner");
    }
}
