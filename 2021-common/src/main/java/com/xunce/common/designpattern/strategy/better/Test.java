package com.xunce.common.designpattern.strategy.better;


/**
 * 使用策略模式 + 工厂模式 + 枚举 + 反射，消除if else
 */
public class Test {
    public static void main(String[] args) throws Exception{

        PayStrategy ali_pay = StrategyFactory.getInstance("ALI_PAY");
        ali_pay.pay(1.12);

    }
}
