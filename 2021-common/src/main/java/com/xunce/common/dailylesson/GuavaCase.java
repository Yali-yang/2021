package com.xunce.common.dailylesson;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.*;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuavaCase {

    public static void main(String[] args) throws Exception {
        // 创建不可变的集合
        ImmutableList<String> list = ImmutableList.of("v1", "v2");
        ImmutableSet<String> set = ImmutableSet.of("v1", "v2");
        ImmutableMap<String, String> map = ImmutableMap.of("k1", "v1", "k2", "v2");

        // 创建一个key是string, value是list的map，并往其中插入元素
        Multimap<String, Integer> listMultimap = ArrayListMultimap.create();
        listMultimap.put("a", 1);
        listMultimap.put("a", 2);
        listMultimap.put("a", 3);

        // 将集合转换为特定规则的字符串
        String str_list = Joiner.on("-").join(list);// v1-v2
        String str_map = Joiner.on(",").withKeyValueSeparator("=").join(map);// k1=v1,k2=v2

        // 将特定的字符串转换成集合  并且去除空格和空的元素  omitEmptyStrings()去除空元素  trimResults：对元素前后去空格
        String tem = "1-2-3-4-5-6--7   ";
        List<String> tem_list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(tem);
        tem = "k1=k2,k2=v2";
        Map<String, String> split = Splitter.on(",").omitEmptyStrings().trimResults().withKeyValueSeparator("=").split(tem);
        // 正则切割字符转集合
        tem = "1-2-3-4-5,6--7   ";
        List<String> strings = Splitter.onPattern("[-|,]").omitEmptyStrings().trimResults().splitToList(tem);

        // 文件操作相关
//        List<String> strings1 = Files.readLines(new File(""), Charsets.UTF_8);

        // 双key map
        Table<String, String, List<Object>> tables = HashBasedTable.create();
        tables.put("财务部", "总监", Lists.newArrayList());
        tables.put("财务部", "职员", Lists.newArrayList());
        tables.put("法务部", "助理", Lists.newArrayList());

        // 1.comparable是自然排序，string类就实现了这个接口，自己和自己比，返回正整数、0、负整数
        // 2.comparator是自定义排序，两个比较的对象是传入的
        Integer i = 2;
        Integer j = 2;
        Ordering<Comparable> natural = Ordering.natural();// Ordering实现了Comparator
        int compare = natural.compare(i, j);

        List<Integer> list2 = Lists.newArrayList(5, 4, 3, 1);
        list2.sort((x, y) -> -Ints.compare(x, y));

        System.out.println();
    }


}
