package com.xunce.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 通过ClassForName的方式调用
 */
public class ClassForNameUtils {
    public static void main(String[] args) {
        try {
            Class clazz = Class.forName("com.xunce.common.utils.ClassForNameUtils");
            Method method = clazz.getMethod("test", String.class);
            method.invoke(clazz.newInstance(), "120120");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void test(String name){
        System.out.println(name);
    }
}
