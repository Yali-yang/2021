package com.xunce.common.shell;

/**
 * 检查接口
 *
 * @author stark
 */
public interface ICheck<T> {

    /**
     * 检查接口
     *
     * @param t param
     * @return true or false
     */
    boolean check(T t);

}
