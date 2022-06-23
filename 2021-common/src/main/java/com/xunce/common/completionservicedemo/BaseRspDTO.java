package com.xunce.common.completionservicedemo;

public class BaseRspDTO<T> {

    private String type;

    private T data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseRspDTO(String type, T data) {
        this.type = type;
        this.data = data;
    }
}
