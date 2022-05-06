package com.xunce.common.designpattern.builder;

/**
 * 建造者模式：将产品和产品建造过程解析 ======> 建造者模式
 * 	4大要素：
 * 		1.产品：House
 * 		2.构建者：HouseBuilder；组合了产品（new了一个房子）由于有各种不同的房子，所以抽象出来，父类定义规范，子类去
 * 			实现不同的盖房子的流程
 * 		3.指挥者：HouseDirector；聚合了构建者（HouseBuilder作为成员变量，但是没有new，通过构造器赋值）谁指挥构建者去建立房子
 * 		4.使用者：Client；我需要盖一个房子，那么我就获取指挥者，去获得一个房子
 */
public class Client {
	public static void main(String[] args) {

		//盖普通房子
		CommonHouse commonHouse = new CommonHouse();
		//准备创建房子的指挥者
		HouseDirector houseDirector = new HouseDirector(commonHouse);

		//完成盖房子，返回产品(普通房子)
		House house = houseDirector.constructHouse();

		System.out.println("--------------------------");
		//盖高楼
		HighBuilding highBuilding = new HighBuilding();
		//重置建造者
		houseDirector.setHouseBuilder(highBuilding);
		//完成盖房子，返回产品(高楼)
		houseDirector.constructHouse();
		
		
		
	}
}
