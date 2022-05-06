package com.xunce.common.designpattern.factory.absfactory.pizzastore.pizza;

//将Pizza 类做成抽象

/**
 * 需要做一个披萨，但是有各种各样的披萨，直接抽象一个父类，定义披萨的名字，子类去实现
 */
public abstract class Pizza {
	protected String name;

	//准备原材料, 不同的披萨不一样，因此，我们做成抽象方法
	public abstract void prepare();

	
	public void bake() {
		System.out.println(name + " baking;");
	}

	public void cut() {
		System.out.println(name + " cutting;");
	}

	//打包
	public void box() {
		System.out.println(name + " boxing;");
	}

	public void setName(String name) {
		this.name = name;
	}
}
