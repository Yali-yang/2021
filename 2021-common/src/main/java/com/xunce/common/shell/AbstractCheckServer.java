package com.xunce.common.shell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stark
 */
public abstract class AbstractCheckServer<T> {


    public List<ICheck> checks = new ArrayList<>();


    public void add(ICheck check) {
        checks.add(check);
    }

    public void remove(ICheck check) {
        checks.remove(check);
    }

    /**
     * 获取校验解耦
     *
     * @param t check object
     * @return true or false
     */
    public abstract boolean getCheckResult(T t);

}
