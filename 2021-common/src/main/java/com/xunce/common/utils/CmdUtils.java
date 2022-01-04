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
        // String cmd = "java -jar D:\\work\\workSpace\\Yung\\2021\\2021-api\\target\\2021-api-1.0-SNAPSHOT.jar";
        InputStream in;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            in = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
            int i = process.waitFor();
            System.out.println("等待时间：" + i);// 等待时间
            process.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
