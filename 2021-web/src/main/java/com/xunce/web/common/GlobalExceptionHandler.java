package com.xunce.web.common;

import com.xunce.common.Result;
import com.xunce.common.enums.ResultEnum;
import com.xunce.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 设置全局捕获异常，省略了代码中各种的try catch
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result handException(BusinessException ex){
        log.info("自定义异常：", ex);
        return Result.failed(ex.getMessage());

    }

    @ExceptionHandler(Exception.class)
    public Result handException(Exception ex){
        log.info("出现异常", ex);
        return Result.failed(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handException(MethodArgumentNotValidException ex){
        log.info("参数校验异常", ex);
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        if (StringUtils.hasText(msg)) {
            return Result.failed(ResultEnum.VALIDATE_FAILED.getCode(), msg);
        }
        return Result.failed(ResultEnum.VALIDATE_FAILED);
    }

}
