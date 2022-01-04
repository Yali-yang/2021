package com.xunce.api;

import com.xunce.common.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LogUtils.initLogback("D:\\work\\workSpace\\Yung\\2021\\2021-api\\src\\main\\resources\\logback-spring.xml");
        logger.info("测试自定义输出日志");
        System.out.println("Hello java -jar");
        if (args == null || args.length <= 0) {
            System.out.println("args为空");
        } else {
            System.out.println("args不为空");
            for (String str : args) {
                System.out.println(str);
            }
        }
    }
}
