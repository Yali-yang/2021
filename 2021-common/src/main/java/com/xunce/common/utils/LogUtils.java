package com.xunce.common.utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class LogUtils {
    private static Logger logger = LoggerFactory.getLogger(LogUtils.class);

    public static void initLogback(String configFilepathName) {
        File logbackFile = new File(configFilepathName);
        try {
            System.out.println("logback配置文件地址：" + logbackFile.getCanonicalPath());
            logger.info("logback配置文件地址");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (logbackFile.exists() && logbackFile.isFile() && logbackFile.canRead()) {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            try {
                configurator.doConfigure(logbackFile);
                logger.info("加载logback配置文件成功！");
            } catch (JoranException e) {
                logger.error("加载logback配置文件失败！", e);
                System.exit(-1);
            }
        } else {
//            log.error("logback配置文件不存在！");
            System.out.println("logback配置文件不存在！");
        }
    }

    /**
     * 加载日志配置文件
     * @param args
     */
    public static void main(String[] args) {
        LogUtils.initLogback("D:\\work\\workSpace\\Yung\\2021\\2021-api\\src\\main\\resources\\logback-spring.xml");
        logger.info("Hello java -jar");
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
