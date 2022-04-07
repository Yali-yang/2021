package com.xunce.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 调用cmd命令
 */
public class CmdUtils {

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
