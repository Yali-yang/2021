package com.xunce.common.designpattern.builder;

/**
 * 抽象的构建者，组合了需要创建的产品、定义了创建产品需要的步骤
 */
public abstract class HouseBuilder {
    protected House house = new House();

    //将建造的流程写好, 抽象的方法
    public abstract void buildBasic();
    public abstract void buildWalls();
    public abstract void roofed();

    //建造房子好， 将产品(房子) 返回
    public House buildHouse() {
        return house;
    }
}
