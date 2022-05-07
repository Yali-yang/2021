package com.xunce.common.designpattern.adapter.classadapter;

//类适配器模式

/**
 * 继承被适配的类
 * 实现需要适配的接口
 * 调用被适配的类的方法，重写实现接口的方法
 *
 * 缺点：
 * 1.java是单继承，能不继承就不继承，可以改为聚合的方式
 */
public class VoltageAdapter extends Voltage220V implements IVoltage5V {

	@Override
	public int output5V() {
		// TODO Auto-generated method stub
		//获取到220V电压
		int srcV = output220V();
		int dstV = srcV / 44 ; //转成 5v
		return dstV;
	}

}

