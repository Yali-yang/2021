package com.xunce.common.completionservicedemo;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 一个并行调用的例子（App首页信息查询）
 * CompletionService实现并行调用
 * 抽取通用的并行调用方法
 * 代码思考以及设计模式应用
 *
 */
public class Client {

    public static void main(String[] args){
        // spring项目，可以新增工厂类实现ApplicationContextAware接口，将IBaseTask的实现类，全部初始化，由工厂类去获取IBaseTask实例化对象
        List<Callable<BaseRspDTO<Object>>> taskList = new ArrayList<>();
        taskList.add(new BaseTask("userInfo" ,new UserInfoStrategyTask()));
        taskList.add(new BaseTask("banner" ,new BannerStrategyTask()));

        List<BaseRspDTO<Object>> baseRspDTOS = executeTask(taskList, Executors.newFixedThreadPool(3), 3);
        System.out.println(JSON.toJSONString(baseRspDTOS));
    }

    public static List<BaseRspDTO<Object>> executeTask(List<Callable<BaseRspDTO<Object>>> taskList, ExecutorService pool, long timeout){
        ExecutorCompletionService service = new ExecutorCompletionService<BaseRspDTO<Object>>(pool);
        taskList.forEach( t -> service.submit(t));

        List<BaseRspDTO<Object>> resultList = new ArrayList<>();
        for (int i = 0; i < taskList.size(); i++) {
            try {
                Future<BaseRspDTO<Object>> poll = service.poll(timeout, TimeUnit.SECONDS);
                resultList.add(poll.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();
        return resultList;
    }

}
