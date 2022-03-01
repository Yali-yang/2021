package com.xunce.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 */
public class ThreadPoolUtils {
    private volatile static ExecutorService instance = null;
    public static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue<Runnable>(100);
    private static Integer corePoolSize = 2;
    private static Integer maximumPoolSize = 4;

    private ThreadPoolUtils(){
    }

    public static ExecutorService getInstance(){
        if(instance == null){
            synchronized (ThreadPoolUtils.class){
                if(instance == null){
                    instance = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                            0L, TimeUnit.SECONDS, linkedBlockingQueue);
                }
            }
        }
        return instance;
    }
}
