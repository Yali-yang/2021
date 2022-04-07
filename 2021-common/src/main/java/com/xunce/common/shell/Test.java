package com.xunce.common.shell;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class Test {
    private static Logger logger = LoggerFactory.getLogger(Test.class);

    /**
     * 这是一个复杂的指向shell命令的方法
     * CmdUtils可以和这个达到一样的效果
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        // 构建执行命令
        String rawScript = buildRawScript("C:\\Users\\tmp\\aaa.excel", "C:\\Users\\hdfs\\aaa.excel", "C:\\Users\\tmp\\json", "UTF-8");
        rawScript = "java -jar D:\\work\\workSpace\\Yung\\2021\\2021-api\\target\\2021-api-1.0-SNAPSHOT.jar";
        String fileName = buildCommand(rawScript);
        System.out.println("fileName:" + fileName);
        AbstractCommandExecutor executor = new AbstractCommandExecutor() {

        };
        CommandExecuteResult run = executor.run(fileName);
        System.out.println(JSON.toJSONString(run));
        OSUtils.exeCmd(rawScript);
    }


    public static String buildCommand(String rawScript) throws Exception {

        File file = File.createTempFile("shell", OSUtils.isWindows() ? ".bat" : ".sh");

        Path path = file.toPath();

        String fileName = file.getCanonicalPath();

        String script = rawScript.replaceAll("\\r\\n", "\n");

        if (OSUtils.isWindows()) {
            script = script.replaceAll("\n", "\r\n");
        }

        rawScript = script;

        Set<PosixFilePermission> perms = PosixFilePermissions.fromString(ShellConstants.RWXR_XR_X);
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);

        if (!OSUtils.isWindows()) {
            Files.setPosixFilePermissions(path, attr.value());
        }

        Files.write(path, rawScript.getBytes());

        return fileName;
    }

    private static String buildRawScript(String... args) {
        StringBuilder sbr = new StringBuilder();
        sbr.append(" ");
        for (String arg : args) {
            sbr.append(" \"").append(arg).append("\" ");
        }
        logger.info("buildRawScript：{}", sbr);
        return sbr.toString();
    }
}
