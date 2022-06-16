package com.xunce.api;


import com.alibaba.fastjson.JSON;
import org.apache.xmlbeans.ResourceLoader;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    @Test
    public void test(){
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("bean.properties");
        Properties properties = new Properties();

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("properties" + JSON.toJSONString(properties));

    }


    public static void main(String[] args) {
    }


}
