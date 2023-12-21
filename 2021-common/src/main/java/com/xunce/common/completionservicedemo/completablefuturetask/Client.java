package com.xunce.common.completionservicedemo.completablefuturetask;


import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {

    public static void main(String[] args) throws Exception{
        ArrayList<String> list = Lists.newArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        CompletableFuture[] futureResultArray = new CompletableFuture[list.size()];
        AtomicInteger index = new AtomicInteger(0);
        list.stream().forEach(t -> {
            CompletableFuture<String> completableFuture = CompletableFuture
                    .supplyAsync(() -> add(t))
                    .exceptionally(e -> "0000");
            futureResultArray[index.getAndIncrement()] = completableFuture;
        });

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(futureResultArray);
//
        voidCompletableFuture.join(); // 加了这个就会等所有的future执行完，main才会继续执行，不加main会继续往下面走

        System.out.println("开始看数据");
        Arrays.stream(futureResultArray).forEach(t -> {
            try {
                System.out.println(t.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        System.out.println("结束");
    }

    public static String add(String param) {
        int ran=new Random().nextInt(10);
        try {
            System.out.println(Thread.currentThread().getName() +  "随机睡" + ran + "秒");
            TimeUnit.SECONDS.sleep(ran);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return param + "1";
    }


}
