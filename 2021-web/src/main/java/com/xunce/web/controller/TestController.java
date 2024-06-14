package com.xunce.web.controller;

import com.xunce.common.Result;
import com.xunce.web.dto.UserAddDto;
import com.xunce.web.exception.BusinessException;
import com.xunce.web.redislock.annotation.RedisLock;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {

    @Resource
    private RedissonClient redissonClient;

    @GetMapping("/test-exception")
    public Result testHandException(){
        throw new BusinessException("出现自定义异常");
    }

    @PostMapping("/test-validate-body")
    public Result testValidate(@RequestBody @Valid UserAddDto dto){

        return Result.success();
    }

    @GetMapping("/test-requestParam")
    public Result testRequestParam(@RequestParam @NotBlank String name){
        System.out.println("测试requestParam校验");
        return Result.success();
    }

    @GetMapping("/test-redisLock1")
    @RedisLock(key = "testRedisLock", errorMessage = "已经加锁了，不要再请求了")
    public Result redisLock1() throws InterruptedException {
        System.out.println("test-redisCache-get:" + Thread.currentThread().getName());

        return Result.success();
    }

}
