package com.xunce.api.iocdemo;

public class Test {
    @org.junit.Test
    public void test(){
        BeanFactory beanFactory = new BeanFactory();
        UserDao userdao = (UserDao)beanFactory.getBean("userdao");
        userdao.query();
    }

}
