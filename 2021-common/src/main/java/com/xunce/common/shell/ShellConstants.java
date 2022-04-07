package com.xunce.common.shell;

import java.util.regex.Pattern;

public class ShellConstants {

    /**
     * string false
     */
    public static final String STRING_FALSE = "false";

    /**
     * check log regex
     */
    public static final Pattern KEY_OF_ERROR = Pattern.compile("(.+?)Error(.+?)");
    public static final Pattern KEY_OF_EXCEPTION = Pattern.compile("(.+?)Exception:(.+?)");


    public static final String RWXR_XR_X = "rwxr-xr-x";

    /**
     * pstree, get pud and sub pid
     */
    public static final String PSTREE = "pstree";

    /**
     * default log cache rows num,output when reach the number
     */
    public static final int DEFAULT_LOG_ROWS_NUM = 4 * 16;

    /**
     * log flush interval?output when reach the interval
     */
    public static final int DEFAULT_LOG_FLUSH_INTERVAL = 1000;

    public static final String PID = OSUtils.isWindows() ? "handle" : "pid";
}
