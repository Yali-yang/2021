package com.xunce.web.aopdemo;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AopService {

    @Log(name = "自定义注解", auth = "User")
    @GetMapping("/test")
    public String aop(String user, String name){
        System.out.println("被调用");
        return "service";
    }

}
