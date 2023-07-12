package com.xunce.common.designpattern.strategy.better;

public class AliPay implements PayStrategy{

    @Override
    public void pay(Double money) {
        System.out.println("阿里支付");
    }
}
