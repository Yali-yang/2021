package com.xunce.common.designpattern.adapter.interfaceadapter;

/**
 * 接口适配器模式
 *
 * 我需要用到Interface4的m1接口，其他的接口我不需要使用
 * AbsAdapter重写Interface4中的所有接口
 * 使用时，创建抽象AbsAdapter的实例，只重写我需要用到的方法
 */
public class Client {
	public static void main(String[] args) {

		AbsAdapter absAdapter = new AbsAdapter() {
			//只需要去覆盖我们 需要使用 接口方法
			@Override
			public void m1() {
				// TODO Auto-generated method stub
				System.out.println("使用了m1的方法");
			}
		};

		absAdapter.m1();
	}
}
