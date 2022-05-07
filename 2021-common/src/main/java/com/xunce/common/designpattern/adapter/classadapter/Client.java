package com.xunce.common.designpattern.adapter.classadapter;

/**
 * 调用适配器
 */
public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(" === 类适配器模式 ====");
		Phone phone = new Phone();
		phone.charging(new VoltageAdapter());
	}

}

