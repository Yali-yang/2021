package com.xunce.common.designpattern.strategy.better;

public enum PayEnum {
    ALI_PAY("com.xunce.common.designpattern.strategy.better.AliPay"),
    WECHAT_PAY("com.xunce.common.designpattern.strategy.better.WeChatPay"),
    ;


    PayEnum(String className) {
        this.className = className;
    }

    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
