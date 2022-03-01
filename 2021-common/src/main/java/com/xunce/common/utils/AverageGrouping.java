package com.xunce.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 对list平均分组,对list进行分割处理
 * iterate：指定一个常量seed，每次递增1，生成limit个元素
 * skip：跳过前n个元素，每次取10个元素，返回一个list
 */
public class AverageGrouping {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26);
        int limit = (list.size() - 1) / 10 + 1;
        List<List<Integer>> averageList = new ArrayList<>();
        Stream.iterate(0, n -> n + 1).limit(limit).forEach(
                i -> {
                    averageList.add(list.stream().skip(i * 10).limit(10).collect(Collectors.toList()));
                }
        );
        // [[1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [11, 12, 13, 14, 15, 16, 17, 18, 19, 20], [21, 22, 23, 24, 25, 26]]
        System.out.println(averageList);
    }

}
