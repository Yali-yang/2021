package com.xunce.common.designpattern.strategy.better;

public class WeChatPay implements PayStrategy{

    @Override
    public void pay(Double money) {
        System.out.println("微信支付");
    }
}
