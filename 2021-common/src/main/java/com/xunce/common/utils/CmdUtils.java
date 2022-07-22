package com.xunce.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 调用cmd命令
 */
@Slf4j
public class CmdUtils {

    public static String execCurl(String[] cmds) {
        String sbss = "";
        for(int i=0;i<cmds.length;i++){
            sbss+=cmds[i];
        }
        log.info("准备开始执行cmd命令 ：{}", sbss);
        ProcessBuilder process = new ProcessBuilder(cmds);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            return builder.toString();
        } catch (IOException e) {
            log.info("执行开始执行cmd命令cmd异常，" ,e);
            return null;
        }
    }

    public static void exec(String cmd){
        if(null == cmd || cmd.trim().length() <= 0){
            System.out.println("cmd命令不能为空");
            return;
        }
//         cmd = "java -jar D:\\work\\workSpace\\Yung\\2021\\2021-api\\target\\2021-api-1.0-SNAPSHOT.jar";
        InputStream in = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            in = process.getInputStream();
            isr = new InputStreamReader(in);
            reader = new BufferedReader(isr);

            String line;
            while((line = reader.readLine()) != null){
                // 这个打印是会把所有在控制台输出的都会打印出来
                System.out.println(line);//返回值
            }
            int i = process.waitFor();
            System.out.println("等待时间：" + i);// 等待时间
            process.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(isr != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        exec("java -jar D:\\work\\workSpace\\Yung\\2021\\2021-api\\target\\2021-api-1.0-SNAPSHOT.jar");
    }
}
