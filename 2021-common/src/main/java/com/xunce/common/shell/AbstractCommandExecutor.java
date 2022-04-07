package com.xunce.common.shell;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ch.qos.logback.classic.ClassicConstants.FINALIZE_SESSION_MARKER;

public abstract class AbstractCommandExecutor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCommandExecutor.class);

    /**
     * For Unix-like, using sh
     */
    public static final String SH = "sh";

    /**
     * For Windows, using cmd.exe
     */
    public static final String CMD = "cmd.exe";

    /**
     * exit code failure
     */
    public static final int EXIT_CODE_FAILURE = -1;

    private Process process;

    private StringBuilder resultStr = new StringBuilder();

    protected boolean logOutputIsSuccess = false;

    protected boolean logHasErrorKey = false;

    protected final List<String> logBuffer;

    public AbstractCommandExecutor() {
        this.logBuffer = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * build process
     *
     * @param commandFile command file
     * @throws IOException IO Exception
     */
    private void buildProcess(String commandFile) throws IOException {
        // setting up user to run commands
        List<String> command = new LinkedList<>();

        if (OSUtils.isWindows()) {
            //throw new RuntimeException("not support windows !");
            //init process builder
            ProcessBuilder processBuilder = new ProcessBuilder();
            // setting up a username and password
            //processBuilder.user(taskExecutionContext.getTenantCode(), StringUtils.EMPTY);
            // merge error information to standard output stream
            processBuilder.redirectErrorStream(true);

            // setting up user to run commands
            command.add(commandInterpreter());
            command.add("/c");
            command.addAll(commandOptions());
            command.add(commandFile);

            // setting commands
            processBuilder.command(command);

            process = processBuilder.start();
        } else {
            //init process builder
            ProcessBuilder processBuilder = new ProcessBuilder();
            // merge error information to standard output stream
            processBuilder.redirectErrorStream(true);

            // setting up user to run commands
            command.add(commandInterpreter());
            command.addAll(commandOptions());
            command.add(commandFile);

            // setting commands
            processBuilder.command(command);
            process = processBuilder.start();
        }

        // print command
        printCommand(command);
    }

    /**
     * task specific execution logic
     *
     * @param execCommand execCommand
     * @return CommandExecuteResult
     * @throws Exception if error throws Exception
     */
    public CommandExecuteResult run(String execCommand) throws Exception {

        CommandExecuteResult result = new CommandExecuteResult();

        String commandFilePath = File.createTempFile("shell", OSUtils.isWindows() ? ".bat" : ".command").getCanonicalPath();

        // create command file if not exists
        createCommandFileIfNotExists(execCommand, commandFilePath);

        //build process
        buildProcess(commandFilePath);

        parseProcessOutput(process);

        Integer processId = getProcessId(process);

        result.setProcessId(processId);

        // print process id
        logger.info("process start, process id is: {}", processId);

        // if timeout occurs, exit directly
        long remainTime = Long.MAX_VALUE;

        // waiting for the run to finish
        boolean status = process.waitFor(remainTime, TimeUnit.SECONDS);
        // if SHELL task exit
        if (status && !logHasErrorKey) {
            // SHELL task state
            result.setExitStatusCode(process.exitValue());
            // 保持结果
            result.setResult(resultStr.toString());
        } else {
            logger.error("process has failure , exitStatusCode : {} , ready to kill ...", result.getExitStatusCode());
            ProcessUtils.kill(processId);
            result.setExitStatusCode(EXIT_CODE_FAILURE);
        }

        return result;
    }


    protected String commandInterpreter() {
        return OSUtils.isWindows() ? CMD : SH;
    }

    protected List<String> commandOptions() {
        return Collections.emptyList();
    }

    private void printCommand(List<String> commands) {
        String cmdStr;

        try {
            cmdStr = ProcessUtils.buildCommandStr(commands);
            logger.info("task run command:\n{}", cmdStr);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * get the standard output of the process
     *
     * @param process process
     */
    private void parseProcessOutput(Process process) {
        BufferedReader inReader = null;
        try {
            inReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            //增加日志校验
            CheckServer checkResultServer = new CheckServer();
            ICheck iCheck = buildResultCheck();
            if (iCheck != null) {
                checkResultServer.add(iCheck);
            }
            String line;
            Boolean checkResult = false;
            while ((line = inReader.readLine()) != null) {
                logger.debug(line);
                if (checkResultServer.getCheckResult(line)) {
                    checkResult = true;
                    line = dealResult(line);
                }
                if (checkResult) {
                    resultStr.append(line);
                }
            }
            logger.info("内容读取完成！");
        } catch (Exception e) {
            logger.error("内容读取失败!{}", e.getMessage(), e);
        } finally {
            logOutputIsSuccess = true;
            close(inReader);
        }
    }

    public <T> ICheck<T> buildResultCheck() {
        return null;
    }

    public String dealResult(String line) {
        return line;
    }

    private int getProcessId(Process process) {
        int processId = 0;

        try {
            Field f = process.getClass().getDeclaredField(ShellConstants.PID);
            f.setAccessible(true);

            if (OSUtils.isWindows()) {
                WinNT.HANDLE handle = new WinNT.HANDLE();
                handle.setPointer(Pointer.createConstant((long) f.get(process)));
                processId = Kernel32.INSTANCE.GetProcessId(handle);
            } else {
                processId = f.getInt(process);
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

        return processId;
    }

    protected void createCommandFileIfNotExists(String execCommand, String commandFile) throws IOException {
        // create if non existence
        logger.info("create command file:{}", commandFile);

        StringBuilder sb = new StringBuilder();
        if (OSUtils.isWindows()) {
            sb.append("@echo off\r\n");
            sb.append("cd /d %~dp0\r\n");
            sb.append("chcp 65001").append("\r\n");
        } else {
            sb.append("BASEDIR=$(cd `dirname $0`; pwd)\n");
            sb.append("cd $BASEDIR\n");
            sb.append("#!/bin/sh\n");
        }

        sb.append(execCommand);
        logger.info("command : {}", sb);

        // write data to file
        FileUtils.writeStringToFile(new File(commandFile), sb.toString(), StandardCharsets.UTF_8);
    }

    /**
     * when log buffer siz or flush time reach condition , then flush
     *
     * @param lastFlushTime last flush time
     * @return last flush time
     */
    /*private long flush(long lastFlushTime) {
        long now = System.currentTimeMillis();

        *//**
         * when log buffer siz or flush time reach condition , then flush
         *//*
        if (logBuffer.size() >= Constants.DEFAULT_LOG_ROWS_NUM || now - lastFlushTime > Constants.DEFAULT_LOG_FLUSH_INTERVAL) {
            lastFlushTime = now;
            logHandle(logBuffer);
            logBuffer.clear();
        }
        return lastFlushTime;
    }*/

    /**
     * clear
     */
    private void clear() {
        List<String> markerList = new ArrayList<>();
        markerList.add(ch.qos.logback.classic.ClassicConstants.FINALIZE_SESSION_MARKER.toString());

        if (!logBuffer.isEmpty()) {
            // log handle
            logHandle(logBuffer);
            logBuffer.clear();
        }
        logHandle(markerList);
    }

    /**
     * log handle
     *
     * @param logs log list
     */
    private void logHandle(List<String> logs) {
        // note that the "new line" is added here to facilitate log parsing
        if (logs.contains(FINALIZE_SESSION_MARKER.toString())) {
            logger.info(FINALIZE_SESSION_MARKER, FINALIZE_SESSION_MARKER.toString());
        } else {
            logger.info(" -> {}", String.join("\n\t", logs));
        }
    }

    /**
     * close buffer reader
     *
     * @param inReader in reader
     */
    private void close(BufferedReader inReader) {
        if (inReader != null) {
            try {
                inReader.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
