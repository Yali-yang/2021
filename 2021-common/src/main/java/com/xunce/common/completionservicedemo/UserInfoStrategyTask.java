package com.xunce.common.completionservicedemo;

public class UserInfoStrategyTask implements IBaseTask{

    @Override
    public String getType() {
        return "UserInfo";
    }

    @Override
    public BaseRspDTO<Object> executeTask() {
        return new BaseRspDTO(getType(), "UserInfo");
    }
}
