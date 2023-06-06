package com.xunce.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.*;
import com.google.common.base.Optional;
import com.google.common.collect.*;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;
import com.jayway.jsonpath.JsonPath;
import com.xunce.common.utils.ThreadPoolUtils;
import net.minidev.json.JSONArray;
import com.mysql.cj.xdevapi.JsonValue;
import com.xunce.common.utils.GuavaCacheUtils;
import com.xunce.common.utils.JsonPathUtils;
import net.minidev.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    /**just
     * 在内层循环里面调用continue（或者break）后接着retry标识符，程序直接转到最外层for循环去处理了
     * url：https://www.cnblogs.com/captainad/p/10931314.html#:~:text=%E7%BD%91%E4%B8%8A%E6%BA%9C%E8%BE%BE%E4%B8%80%E7%95%AA%E5%8F%91%E7%8E%B0,%E7%9C%8B%E7%9C%8B%E4%B8%8B%E9%9D%A2%E7%9A%84%E4%BE%8B%E5%AD%90%EF%BC%9A
     */
    @Test
    public void testRetry() {
        retry:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(j + ", ");
                if (j == 3) {
                    break retry;
                }
            }
        }
        System.out.println(" >>> OK");
    }

    /**
     * url ： https://blog.csdn.net/qq_33204709/article/details/127790693
     * <p>
     * 用途： 用于检索一个元素是否在一个集合中。
     * <p>
     * 优点：
     * <p>
     * 时间复杂度低，增加及查询元素的时间复杂度都是O(k)，k为Hash函数的个数；
     * 占用存储空间小，布隆过滤器相对于其他数据结构（如Set、Map）非常节省空间。
     * 缺点：
     * <p>
     * 存在误判，只能证明一个元素一定不存在或者可能存在，返回结果是概率性的，但是可以通过调整参数来降低误判比例；
     * 删除困难，一个元素映射到bit数组上的k个位置为1，删除的时候不能简单的直接置为0，可能会影响到其他元素的判断。
     */
    @Test
    public void testBloomFilter() {
        // 初始化布隆过滤器，设计预计元素数量为1000000L，误差率为1%
        BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), 1000000, 0.01);
        String str = "abc";
        boolean put = bloomFilter.put(str);
        boolean b = bloomFilter.mightContain(str);
        boolean g = bloomFilter.mightContain("fasdfas");

//        int n = 1000000;
//        for (int i = 0; i < n; i++) {
//            bloomFilter.put(String.valueOf(i));
//        }
//        int count = 0;
//        for (int i = 0; i < (n * 2); i++) {
//            if (bloomFilter.mightContain(String.valueOf(i))) {
//                count++;
//            }
//        }
//        System.out.println("过滤器误判率：" + 1.0 * (count - n) / n);
    }

    @Test
    public void testString() throws NoSuchFieldException, IllegalAccessException {
        // 通过反射去修改string的值
        String str = "abc";
        System.out.println(str.hashCode());// 96354
        Field field = String.class.getDeclaredField("value");
        field.setAccessible(true);
        field.set(str, new char[]{'中', '国'});
        System.out.println(str.hashCode());// 96354

        // 堆中有new String("aa")的对象，常量池中没有aa对象
        String s = new String("a") + new String("a");
        // 虽然常量池没有aa，但是堆中有，所以再常量池新建一个对象，存的是堆中new String("aa")地址
        // 如果先执行String s3 = "aa";就直接在常量池中创建aa了
        String s2 = s.intern();
        // 发现堆中已经有了，直接去了，实际上取的是堆中的地址
        String s3 = "aa";

        System.out.println(s == s3);// true
        System.out.println(s2 == s3);// true


    }

    @Test
    public void testGuava() throws IOException {
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
        String str_list = Joiner.on("-").join(list);
        String str_map = Joiner.on(",").withKeyValueSeparator("=").join(map);

        // 将特定的字符串转换成集合  并且去除空格和空的元素  omitEmptyStrings()去除空元素  trimResults：对元素前后去空格
        String tem = "1-2-3-4-5-6--7   ";
        List<String> tem_list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(tem);
        tem = "k1=k2,k2=v2";
        Map<String, String> split = Splitter.on(",").withKeyValueSeparator("=").split(tem);
        // 正则切割字符转集合
        tem = "1-2-3-4-5,6--7   ";
        List<String> strings = Splitter.onPattern("[-|,]").omitEmptyStrings().trimResults().splitToList(tem);

        // 计算代码运行时间
        Stopwatch started = Stopwatch.createStarted();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time = started.elapsed(TimeUnit.MILLISECONDS);

        // 文件操作相关
        List<String> strings1 = Files.readLines(new File(""), Charsets.UTF_8);

        // 双key map
        Table<String, String, List<Object>> tables = HashBasedTable.create();
        tables.put("财务部", "总监", Lists.newArrayList());
        tables.put("财务部", "职员", Lists.newArrayList());
        tables.put("法务部", "助理", Lists.newArrayList());
        // 本地缓存  用hashMap也可以实现
        GuavaCacheUtils.put("k1", "v1");
        String k1 = GuavaCacheUtils.get("k1");
        // 排序
        // 1.comparable是自然排序，string类就实现了这个接口，自己和自己比，返回正整数、0、负整数
        // 2.comparator是自定义排序，两个比较的对象是传入的
        Integer i = 1;
        Integer j = 2;
        Ordering<Comparable> natural = Ordering.natural();

        Ordering<Integer> from = Ordering.from((x, y) -> Ints.compare(x, y));
        ArrayList<Integer> list2 = Lists.newArrayList(1);
        list2.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Ints.compare(o1, o2);
            }
        });

        list2.sort((x, y) -> Ints.compare(x, y));
    }


}
