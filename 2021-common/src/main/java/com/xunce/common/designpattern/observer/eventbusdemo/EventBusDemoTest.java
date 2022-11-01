package com.xunce.common.designpattern.observer.eventbusdemo;

public class EventBusDemoTest {

    public static void main(String[] args) {

        EventListener eventListener = new EventListener();
        EventBusCenter.register(eventListener);
        EventBusCenter.post(new NotifyEvent("13372817283", "123@qq.com", "666"));
    }

}
