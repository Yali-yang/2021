package com.xunce.web;

import com.xunce.web.aopdemo.AopService;
import com.xunce.web.entity.Yung;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    Yung yung;
    @Autowired
    AopService aopService;

    @Test
    void contextLoads() {
//        yung.setName("fdsaf");
//        String name = yung.getName();
//        System.out.println(name);
        aopService.aop("yung", "fasd");
    }

}
