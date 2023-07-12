package com.xunce.common.designpattern.strategy.better;

public class StrategyFactory {

    public static PayStrategy getInstance(String payEnum) throws Exception {
        return (PayStrategy) Class.forName(PayEnum.valueOf(payEnum).getClassName()).newInstance();
    }

}
