package com.xunce.common.designpattern.factory.absfactory.pizzastore.order;


/**
 * 需求：我是商店。我要定购一个披萨，用抽象工厂方法实现
 * 1.我找到OrderPizza，告诉它我要订购披萨的种类
 * 2.OrderPizza聚合了AbsFactory
 * 3.AbsFactory根据传入的orderType生成不同的披萨
 * 4.我调用OrderPizza，并传入我需要那个工厂来创建披萨，
 * 		工厂根据我传入的orderType生成对应的披萨给我
 *
 * 	总结：
 * 		1.不是接口就是抽象类，增加扩展性，不要继承
 * 		2.简单工厂模式是直接通过一个类返回对象
 * 			抽象工厂模式是把工厂再抽象一层。全部面向接口编程
 *
 *
 */
public class PizzaStore {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//new OrderPizza(new BJFactory());
		new OrderPizza(new LDFactory());
	}

}
