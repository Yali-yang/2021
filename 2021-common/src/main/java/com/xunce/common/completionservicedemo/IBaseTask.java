package com.xunce.common.completionservicedemo;

public interface IBaseTask {

    String getType();

    BaseRspDTO<Object> executeTask();

}
