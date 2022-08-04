package com.xunce.api;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import org.junit.Test;

import java.math.BigDecimal;

public class Main {
    @Test
    public void test(){
        String str = "%s/%s/%s";
        String format = String.format(str, "a", "b", 1);
        System.out.println(str);
        System.out.println(format);
    }


    public static void main(String[] args) {
        BigDecimal bigDecimal = BigDecimal.valueOf(0.02);
    }


}
