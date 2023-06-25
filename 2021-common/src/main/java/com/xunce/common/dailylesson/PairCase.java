package com.xunce.common.dailylesson;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

public class PairCase {
    public static final Pair<Boolean, String> SUCCESS_RESULT = Pair.of(true, StringUtils.EMPTY);

    public static void main(String[] args) {
        Triple<Boolean, String, Integer> triple = Triple.of(false, "", 1);
        Boolean left = triple.getLeft();
        String middle = triple.getMiddle();
        Integer right = triple.getRight();

        Supplier<Pair<Boolean, String>> supplier = and(() -> Pair.of(true, ""), () -> Pair.of(false, "名称不能为空"));
        Pair<Boolean, String> pair = supplier.get();
        if(!pair.getLeft()){
            System.out.println(pair.getRight());
        }


        System.out.println("--------------------这是一个分割线--------------------");


        Pair<Boolean, String> and = and(Pair.of(true, ""), Pair.of(true, "名字不能为空"), Pair.of(false, "年龄不能为空"));
        System.out.println(and.getRight());

    }


    /**
     * 用and组装校验器，返回组合的校验器
     *
     * @param checkerArr 校验器列表
     * @return 组合的校验器，列表为空或者存在null元素，返回null
     */
    @SafeVarargs
    public static Supplier<Pair<Boolean, String>> and(Supplier<Pair<Boolean, String>>... checkerArr) {
        if (ArrayUtils.isEmpty(checkerArr) || Arrays.stream(checkerArr).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("参数不能为空");
        }

        // reduce的意思：第一步，把SUCCESS_RESULT作为第一次accChecker的值，和checkerArr中第一个元素作为currentChecker，这是两个入参，最终返回的还是Supplier<Pair<Boolean, String>>
        // 当前是对Supplier<Pair<Boolean, String>>进行流式计算，那么，reduce的两个函数的返回值都是Supplier
        // (accChecker, currentChecker)是两个入参，后续返回的是Supplier，又用了一次Lamda表达式
        return Arrays.stream(checkerArr).reduce(() -> SUCCESS_RESULT, (accChecker, currentChecker) -> () -> {
            Pair<Boolean, String> accResult = accChecker.get();
            // 出现了一个为false，取反为true的情况，会继续循环完，但是每次的accChecker，都是这个accResult的
            if (!accResult.getLeft()) {
                return accResult;
            } else {
                return currentChecker.get();
            }
        });
    }

    /**
     * 对上面的优化，可以不使用Supplier
     * @param checkerArr
     * @return
     */
    public static Pair<Boolean, String> and(Pair<Boolean, String>... checkerArr){
        if(ArrayUtils.isEmpty(checkerArr) || Arrays.stream(checkerArr).anyMatch(Objects::isNull)){
            throw new IllegalArgumentException("参数不能为空");
        }

        return Arrays.stream(checkerArr).reduce(SUCCESS_RESULT, (accChecker, currChecker) -> {
            if (!accChecker.getLeft()) {
                return accChecker;
            }
            return currChecker;
        });
    }

}
