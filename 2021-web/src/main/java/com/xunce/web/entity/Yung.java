package com.xunce.web.entity;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * bean的初始化过程
 */
public class Yung implements BeanNameAware, BeanFactoryAware, InitializingBean, DisposableBean {

    String name;

    public Yung() {
        System.out.println("1.Yung构造函数.......");
        setName("");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("2.设置name属性......");
        this.name = name;
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("3.BeanNameAware，设置BeanName.........");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("4.BeanFactoryAware，设置BeanFactory.........");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("6.InitializingBean，设置PropertiesSet.........");
    }

    public void initMethod(){
        System.out.println("7.自定义初始化方法被调用initMethod.........");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("9.DisposableBean，destroy.........");
    }

    public void destroyMethod(){
        System.out.println("10.自定义销毁方法被调用destroyMethod.........");
    }
}
