package com.xunce.common.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author stark
 */

public class CheckServer<T> extends AbstractCheckServer<T> {

    private static final Logger logger = LoggerFactory.getLogger(CheckServer.class);

    /**
     * 返回true表示校验不通过，返回false表示校验通过
     *
     * @return true or false
     */
    @Override
    public boolean getCheckResult(T t) {
        if (checks == null || checks.size() == 0) {
            return false;
        }
        boolean flag = false;
        try {
            for (ICheck icheck : checks) {
                //如果有一个校验不通过，则返回
                if (icheck.check(t)) {
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            logger.warn("CheckServer异常 {}", e.getMessage());
        }
        return flag;
    }
}
