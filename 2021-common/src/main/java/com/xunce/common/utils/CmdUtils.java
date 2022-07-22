package com.xunce.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 调用cmd命令
 */
@Slf4j
public class CmdUtils {

    /**
     *  1.生成sh文件
     *  2.将命令写入sh文件
     *  3.sh 调用命令文件
     *  4.删除sh文件
     * @param cmd
     */
    public static void execSh(String cmd){
        // cmd = "java -jar D:\\work\\workSpace\\Yung\\2021\\2021-api\\target\\2021-api-1.0-SNAPSHOT.jar";
        String filePath = "/tmp/sms_" + System.currentTimeMillis() + ".sh";
        File cmdFile = new File(filePath);
        Path path = Paths.get(filePath);
        log.info("准备开始执行sh命令  方法3 ：filePath = {} ,path = {}", filePath, path.getFileName());
        try {
            cmdFile.createNewFile();
            cmdFile.setReadable(true, true);
            cmdFile.setWritable(true, true);
            cmdFile.setExecutable(true, true);
            Files.write(path, cmd.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.info("创建sh脚本失败。。 e = {}", e);
        }
        log.info("准备开始执行sh命令 sh脚本创建成功，开始执行  方法3 ：filePagt = {} ,, path = {}", filePath, path.getFileName());
        CmdUtils.exec("sh " + filePath);
        log.info("执行sh命令 执行完成  方法3 ：filePath = {} ,path = {}", filePath, path.getFileName());

        cmdFile.delete();

    }

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
