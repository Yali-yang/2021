package com.xunce.web.controller;

import com.xunce.common.Result;
import com.xunce.web.dto.UserAddDto;
import com.xunce.web.exception.BusinessException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
public class TestController {

    @GetMapping("/test-exception")
    public Result testHandException(){
        System.out.println("test-exception");
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

}
