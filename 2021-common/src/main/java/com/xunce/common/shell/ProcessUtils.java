package com.xunce.common.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mainly used to get the start command line of a process.
 */
public class ProcessUtils {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);

    /**
     * Initialization regularization, solve the problem of pre-compilation performance,
     * avoid the thread safety problem of multi-thread operation.
     */
    private static final Pattern MACPATTERN = Pattern.compile("-[+|-]-\\s(\\d+)");

    private static final Pattern LINUXPATTERN = Pattern.compile("(\\d+)");

    private static final String LOCAL_PROCESS_EXEC = "jdk.lang.Process.allowAmbiguousCommands";

    /**
     * build command line characters.
     *
     * @param commandList command list
     * @return command
     * @throws IOException io exception
     */
    public static String buildCommandStr(List<String> commandList) throws IOException {
        String cmdstr;
        String[] cmd = commandList.toArray(new String[0]);
        SecurityManager security = System.getSecurityManager();
        boolean allowAmbiguousCommands = isAllowAmbiguousCommands(security);
        if (allowAmbiguousCommands) {

            String executablePath = new File(cmd[0]).getPath();

            if (needsEscaping(VERIFICATION_LEGACY, executablePath)) {
                executablePath = quoteString(executablePath);
            }

            cmdstr = createCommandLine(
                    VERIFICATION_LEGACY, executablePath, cmd);
        } else {
            String executablePath;
            try {
                executablePath = getExecutablePath(cmd[0]);
            } catch (IllegalArgumentException e) {

                StringBuilder join = new StringBuilder();
                for (String s : cmd) {
                    join.append(s).append(' ');
                }

                cmd = getTokensFromCommand(join.toString());
                executablePath = getExecutablePath(cmd[0]);

                // Check new executable name once more
                if (security != null) {
                    security.checkExec(executablePath);
                }
            }

            cmdstr = createCommandLine(

                    isShellFile(executablePath) ? VERIFICATION_CMD_BAT : VERIFICATION_WIN32, quoteString(executablePath), cmd);
        }
        return cmdstr;
    }

    /**
     * check is allow ambiguous commands
     *
     * @param security security manager
     * @return allow ambiguous command flag
     */
    private static boolean isAllowAmbiguousCommands(SecurityManager security) {
        boolean allowAmbiguousCommands = false;
        if (security == null) {
            allowAmbiguousCommands = true;
            String value = System.getProperty(LOCAL_PROCESS_EXEC);
            if (value != null) {
                allowAmbiguousCommands = !ShellConstants.STRING_FALSE.equalsIgnoreCase(value);
            }
        }
        return allowAmbiguousCommands;
    }

    /**
     * get executable path.
     *
     * @param path path
     * @return executable path
     * @throws IOException io exception
     */
    private static String getExecutablePath(String path) throws IOException {
        boolean pathIsQuoted = isQuoted(true, path, "Executable name has embedded quote, split the arguments");

        File fileToRun = new File(pathIsQuoted ? path.substring(1, path.length() - 1) : path);
        return fileToRun.getPath();
    }

    /**
     * whether is shell file.
     *
     * @param executablePath executable path
     * @return true if endsWith .CMD or .BAT
     */
    private static boolean isShellFile(String executablePath) {
        String upPath = executablePath.toUpperCase();
        return (upPath.endsWith(".CMD") || upPath.endsWith(".BAT"));
    }

    /**
     * quote string
     *
     * @param arg argument
     * @return format arg
     */
    private static String quoteString(String arg) {
        StringBuilder argbuf = new StringBuilder(arg.length() + 2);
        return argbuf.append('"').append(arg).append('"').toString();
    }

    /**
     * get tokens from command.
     *
     * @param command command
     * @return token string array
     */
    private static String[] getTokensFromCommand(String command) {
        ArrayList<String> matchList = new ArrayList<>(8);
        Matcher regexMatcher = LazyPattern.PATTERN.matcher(command);
        while (regexMatcher.find()) {
            matchList.add(regexMatcher.group());
        }
        return matchList.toArray(new String[0]);
    }

    /**
     * Lazy Pattern
     */
    private static class LazyPattern {
        /**
         * Escape-support version:
         * "(\")((?:\\\\\\1|.)+?)\\1|([^\\s\"]+)";
         */
        private static final Pattern PATTERN = Pattern.compile("[^\\s\"]+|\"[^\"]*\"");
    }

    /**
     * verification cmd bat
     */
    private static final int VERIFICATION_CMD_BAT = 0;

    /**
     * verification win32
     */
    private static final int VERIFICATION_WIN32 = 1;

    /**
     * verification legacy
     */
    private static final int VERIFICATION_LEGACY = 2;

    /**
     * escape verification
     */
    private static final char[][] ESCAPE_VERIFICATION = {{' ', '\t', '<', '>', '&', '|', '^'},

            {' ', '\t', '<', '>'}, {' ', '\t'}};

    /**
     * create command line
     *
     * @param verificationType verification type
     * @param executablePath   executable path
     * @param cmd              cmd
     * @return command line
     */
    private static String createCommandLine(int verificationType, final String executablePath, final String[] cmd) {
        StringBuilder cmdbuf = new StringBuilder(80);

        cmdbuf.append(executablePath);

        for (int i = 1; i < cmd.length; ++i) {
            cmdbuf.append(' ');
            String s = cmd[i];
            if (needsEscaping(verificationType, s)) {
                cmdbuf.append('"').append(s);

                if ((verificationType != VERIFICATION_CMD_BAT) && s.endsWith("\\")) {
                    cmdbuf.append('\\');
                }
                cmdbuf.append('"');
            } else {
                cmdbuf.append(s);
            }
        }
        return cmdbuf.toString();
    }

    /**
     * whether is quoted
     *
     * @param noQuotesInside no quotes inside
     * @param arg            arg
     * @param errorMessage   error message
     * @return boolean
     */
    private static boolean isQuoted(boolean noQuotesInside, String arg, String errorMessage) {
        int lastPos = arg.length() - 1;
        if (lastPos >= 1 && arg.charAt(0) == '"' && arg.charAt(lastPos) == '"') {
            // The argument has already been quoted.
            if (noQuotesInside && arg.indexOf('"', 1) != lastPos) {
                // There is ["] inside.
                throw new IllegalArgumentException(errorMessage);
            }
            return true;
        }
        if (noQuotesInside && arg.indexOf('"') >= 0) {
            // There is ["] inside.
            throw new IllegalArgumentException(errorMessage);
        }
        return false;
    }

    /**
     * whether needs escaping
     *
     * @param verificationType verification type
     * @param arg              arg
     * @return boolean
     */
    private static boolean needsEscaping(int verificationType, String arg) {

        boolean argIsQuoted = isQuoted((verificationType == VERIFICATION_CMD_BAT), arg, "Argument has embedded quote, use the explicit CMD.EXE call.");

        if (!argIsQuoted) {
            char[] testEscape = ESCAPE_VERIFICATION[verificationType];
            for (char c : testEscape) {
                if (arg.indexOf(c) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * kill tasks according to different task types
     *
     * @param processId
     */
    public static void kill(int processId) {
        try {
            if (processId == 0) {
                return;
            }

            String cmd;
            if(OSUtils.isWindows()){
                cmd = String.format("taskkill /pid %d -t -f", processId);
            } else {
                cmd = String.format("sudo kill -9 %s", getPidsStr(processId));
            }

            logger.info("process id:{}, cmd:{}", processId, cmd);

            OSUtils.exeCmd(cmd);

        } catch (Exception e) {
            logger.error("kill task failed", e);
        }
    }

    /**
     * get pids str
     *
     * @param processId process id
     * @return pids pid String
     * @throws Exception exception
     */
    public static String getPidsStr(int processId) throws Exception {
        List<String> pidList = new ArrayList<>();
        Matcher mat = null;
        // pstree pid get sub pids
        if (OSUtils.isMacOS()) {
            String pids = OSUtils.exeCmd(String.format("%s -sp %d", ShellConstants.PSTREE, processId));
            if (null != pids) {
                mat = MACPATTERN.matcher(pids);
            }
        } else {
            String pids = OSUtils.exeCmd(String.format("%s -p %d", ShellConstants.PSTREE, processId));
            mat = LINUXPATTERN.matcher(pids);
        }

        if (null != mat) {
            while (mat.find()) {
                pidList.add(mat.group(1));
            }
        }
        if (!pidList.isEmpty()) {
            pidList = pidList.subList(1, pidList.size());
        }
        return String.join(" ", pidList).trim();
    }

}
