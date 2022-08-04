package com.xunce.common.utils;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildTreeUtils {
    /**
     * 将id-pid转为tree结构，单一顶层节点，顶层节点的id和pid不能相同
     * 顶层只有一个节点
     * @param pidList
     * @return
     */
    public TestEntity buildTree1(List<TestEntity> pidList){
        //以pid为Key进行分组存入Map
        Map<Integer,List<TestEntity>> pidListMap =
                pidList.stream().collect(Collectors.groupingBy(TestEntity::getPid));
        pidList.stream().forEach(item->item.setChildren(pidListMap.get(item.getId())));
        //取出顶层节点的对象，本例顶层节点的"PID"为0,注意是PID
        return pidListMap.get(0).get(0);
    }

    /**
     * 顶层只有多个节点
     * @param pidList
     * @return
     */
    public List<TestEntity> buildTree2(List<TestEntity> pidList){
        Map<Integer,List<TestEntity>> pidListMap =
                pidList.stream().collect(Collectors.groupingBy(TestEntity::getPid));
        pidList.stream().forEach(item->item.setChildren(pidListMap.get(item.getId())));
        //返回结果也改为返回顶层节点的list
        return pidListMap.get(0);
    }

    @Data
    class TestEntity{
        /** 必备的结构属性 **/
        //id
        private int id;
        //父id
        private int pid;
        //子集合
        private List<TestEntity> children;

        /** 私有属性，与业务相关的属性 **/
        //名称
        private String name;
    }
}
