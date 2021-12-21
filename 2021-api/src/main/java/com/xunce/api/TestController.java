package com.xunce.api;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;

public class TestController {


    public static void main(String[] args) {

        System.out.println(JSON.toJSONString(Arrays.asList("Hello", "golang")));

    }
}
