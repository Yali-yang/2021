package com.xunce.common.dailylesson;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlatMapCase {

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> arrayLists =
                Lists.newArrayList(Lists.newArrayList(1, 2, 3), Lists.newArrayList(4, 5, 6));

        // 把arrayLists中的元素，本来是一个的list，但是flatmap把这个list拆开了，一个子元素成了一个流，拆分流的操作
        // 目的就是不要给我返回List<List<Integer>>，而是给我返回List<Integer>，flagMap要做的就是把List<Integer>给我拆开
        // 其实可以再加一层，多写一层flatmap即可
        List<Integer> collect = arrayLists.stream().flatMap(t -> t.stream()).collect(Collectors.toList());



    }

}
