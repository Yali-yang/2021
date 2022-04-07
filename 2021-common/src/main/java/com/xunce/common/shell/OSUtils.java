/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xunce.common.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.StringTokenizer;

/**
 * os utils
 */
public class OSUtils {

    private static final Logger logger = LoggerFactory.getLogger(OSUtils.class);

    public static final ThreadLocal<Logger> taskLoggerThreadLocal = new ThreadLocal<>();

    private OSUtils() {
    }

    /**
     * get process id
     *
     * @return process id
     */
    public static int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.parseInt(runtimeMXBean.getName().split("@")[0]);
    }

    /**
     * whether is macOS
     *
     * @return true if mac
     */
    public static boolean isMacOS() {
        return getOSName().startsWith("Mac");
    }

    /**
     * whether is windows
     *
     * @return true if windows
     */
    public static boolean isWindows() {
        return getOSName().startsWith("Windows");
    }

    /**
     * get current OS name
     *
     * @return current OS name
     */
    public static String getOSName() {
        return System.getProperty("os.name");
    }


    public static String exeCmd(String command) throws IOException {
        StringTokenizer st = new StringTokenizer(command);
        String[] cmdArray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            cmdArray[i] = st.nextToken();
        }
        return exeShell(cmdArray);
    }

    /**
     * Execute the shell
     *
     * @param command command
     * @return result of execute the shell
     * @throws IOException errors
     */
    public static String exeShell(String[] command) throws IOException {
        return ShellExecutor.execCommand(command);
    }
}
