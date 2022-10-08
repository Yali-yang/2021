package com.xunce.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static void main(String[] args) {
        if(args != null && args.length > 0){
            for (String arg : args) {
                System.out.println(arg);
            }
        }
        String path = "C:\\Users\\admin\\Desktop\\2022\\任务\\1kill\\.*\\.*";
        String[] pathLayerArr = path.split("\\\\");
        File dir = new File(pathLayerArr[0]);
        List<String> fileNames = new ArrayList<>();
        doFindFileList(dir, fileNames, pathLayerArr, 1);
        System.out.println(fileNames);

    }

    /**
     * 适用场景：给一个path，这个path是包含正则的，匹配出对应的文件
     * @param dir
     * @param fileNames
     * @param pathLayerArr
     * @param layerNum
     *
     */
    public static void doFindFileList(File dir, List<String> fileNames, String[] pathLayerArr, Integer layerNum) {
        if (!dir.exists() || !dir.isDirectory()) {// 判断是否存在目录
            return;
        }
        String[] files = dir.list();// 读取目录下的所有目录文件信息
        for (int i = 0; i < files.length; i++) {// 循环，添加文件名或回调自身
            File file = new File(dir, files[i]);
            if (file.isFile()) {// 如果文件
                if(layerNum != pathLayerArr.length - 1){
                    continue;
                }
                String fileName = pathLayerArr[layerNum];
                //文件名匹配
                if (file.getName().matches(fileName)) {
                    fileNames.add(dir + File.separator + file.getName());// 添加文件全路径名
                }
            } else {// 如果是目录
                //目录名称
                String dirName = file.getName();
                //层数要小于等于路径的层数
                if (layerNum <= pathLayerArr.length - 1) {
                    //获取当前层数了路径
                    String currentLayerPath = pathLayerArr[layerNum];
                    //目录名称匹配当前层级路径
                    if (dirName.matches(currentLayerPath)) {
                        doFindFileList(file, fileNames, pathLayerArr, layerNum + 1);// 回调自身继续查询
                    }
                }
            }
        }
    }


    /**
     * 读取目录下的所有文件
     *
     * @param dir       目录
     * @param fileNames 保存文件名的集合
     * @return
     */
    public static void doFindFileList(File dir, List<String> fileNames) {
        if (!dir.exists() || !dir.isDirectory()) {// 判断是否存在目录
            return;
        }
        String[] files = dir.list();// 读取目录下的所有目录文件信息
        for (int i = 0; i < files.length; i++) {// 循环，添加文件名或回调自身
            File file = new File(dir, files[i]);
            if (file.isFile()) {// 如果文件
                fileNames.add(dir + File.separator + file.getName());// 添加文件全路径名
            } else {// 如果是目录
                doFindFileList(file, fileNames);// 回调自身继续查询
            }
        }
    }
}
