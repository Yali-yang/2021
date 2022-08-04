package com.xunce.web.common;

import com.xunce.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 设置全局捕获异常，省略了代码中各种的try catch
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result handException(){
        return Result.error("全局捕获异常");
    }

}
