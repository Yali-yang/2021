package com.xunce.web.aopdemo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
public class LogAspect {

    @Pointcut("@annotation(com.xunce.web.aopdemo.Log)")
    private void pointcut(){}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Exception{
        System.out.println("进入aop");
//        Log log = AnnotationUtils.getAnnotation((Annotation) point.getSignature(), Log.class);
        // 方法的参数
        Object[] args = point.getArgs();

        // 获取注解的值
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = point.getTarget().getClass().getMethod(signature.getName(), signature.getParameterTypes());
        Log annotation = method.getAnnotation(Log.class);


        Object proceed = null;
        try {
            // 放行
            proceed = point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("离开aop");
        return proceed;
    }

}
