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
import com.google.common.util.concurrent.RateLimiter;
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
import org.apache.poi.hpsf.ReadingNotSupportedException;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Main {

    /**
     * 单链表反转
     */
    public class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while(curr != null){
            ListNode temp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = temp;
        }

        return prev;

    }




    /**
     * 翻转一个二叉树-递归
     * 记住：
     * 终止条件
     * 递归函数
     */
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public TreeNode invertTree(TreeNode root) {
        if (root == null || (root.left == null && root.right == null)) {
            return root;
        }

        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);

        root.left = right;
        root.right = left;

        return root;
    }

    /**
     * 动态规划-青蛙跳楼梯
     */
    @Test
    public void testDynamic() {
        System.out.println(numWays(10));
    }

    /**
     * 动态规划-青蛙跳楼梯
     * 问题：一只青蛙一次可以跳上1级台阶，也可以跳上2级台阶。求该青蛙跳上一个 n 级的台阶总共有多少种跳法。
     * 动态规划：其实就是，给定一个问题，我们把它拆成一个个子问题，直到子问题可以直接解决。然后呢，把子问题答案保存起来，以减少重复计算。再根据子问题答案反推，得出原问题解的一种方法。
     */
    public int numWays(int n) {
        if (n <= 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        // a : f(1) = 1
        // b : f(2) = 2
        int a = 1;
        int b = 2;
        int temp = 0;
        /**
         * 如果n=4；第一次循环是发f3 = f1 +f2的，也就是a+b；
         *         第二次循环，i = 4;f4=f2+f3的。此时，a=f2=2  b=f3=3，因为在上一个循环中对a和b重新赋值了
         *下面的循环就不用再管，容易绕进去
         *
         * 记住：
         * f(1) = 1, f(2) = 2 就是边界啦
         *f(n-1)和f(n-2) 称为 f(n) 的最优子结构   a和b 其实就是f(n-1)和f(n-2)
         *比如f(10)= f(9)+f(8),f(9) = f(8) + f(7) ,f(8)就是重叠子问题。 需要临时变量记录值，不要重复算，a、b、temp就是为了记录值
         */
        for (int i = 3; i <= n; i++) {
            temp = (a + b);
            a = b;
            b = temp;
        }
        return temp;
    }

    @Test
    public void testCompletableFuture5() throws ExecutionException, InterruptedException {
        // 计算代码运行时间
        Stopwatch started = Stopwatch.createStarted();

        // thenCombine 的使用   获取两个线程的值的值并处理
        CompletableFuture<String> taskA = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "A";
        });

        CompletableFuture<String> taskB = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "B";
        });

        CompletableFuture<String> stringCompletableFuture = taskA.thenCombine(taskB, (x, y) -> {
            return x + y;
        });
        System.out.println("结果 ：：：" + stringCompletableFuture.join());
        System.out.println("时间 ：：：" + started.elapsed(TimeUnit.MILLISECONDS));

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCompletableFuture4() throws ExecutionException, InterruptedException {
        // applyToEither 的使用   两个线程，那个快选那个
        CompletableFuture<String> taskA = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "A";
        });

        CompletableFuture<String> taskB = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "B";
        });

        // applyToEither 的使用，从A和B中选一个快的，并将快的那个的返回值，作为第二个参数的入参
        CompletableFuture<String> applyToEither = taskA.applyToEither(taskB, t -> {
            System.out.println(t + " is win");
            return "C";
        });
        System.out.println(applyToEither);


        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCompletableFuture3() throws ExecutionException, InterruptedException {
        // 通过CompletableFuture多线程去执行
        long start = System.currentTimeMillis();
        List<Book> books = Arrays.asList(new Book("2356"), new Book("8654"));
        List<String> collect = books.stream()
                .map(t -> CompletableFuture.supplyAsync(() -> t.getPrice()))
                .collect(Collectors.toList())
                .stream()
                .map(t -> t.join())
                .collect(Collectors.toList());
        System.out.println(collect);
        System.out.println("时间 ：：：" + (System.currentTimeMillis() - start));

    }

    class Book {
        String price;

        public String getPrice() {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Book(String price) {
            this.price = price;
        }
    }

    @Test
    public void testCompletableFuture2() throws ExecutionException, InterruptedException {
        // whenComplete 和 exceptionally 的使用
        // thenApply 和 handle 使用，获取上一步的结果，并需要返回
        // thenAccept 消费上一步的结果，不需要返回
        // thenRun 不需要上一个的执行结果，接着执行
        // 以上都是串行化执行
        ExecutorService pool = Executors.newFixedThreadPool(2);

        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello";
        }, pool).thenApply(t -> {
            // 获取上一个的执行结果，串行化处理
            // 如果出现异常了，直接进入whenComplete 和 exceptionally，两个进入
            int i = 10 / 0;
            return t + " world";
        }).handle((t, e) -> {
            // 获取上一个的执行结果，串行化处理，由于有e去接受异常，就算上个任务出现异常，任然可以走到这一步
            return t + " handle";
        }).thenAccept(t -> {
            // 消费接口，无返回值
            System.out.println(t);
        }).whenComplete((v, e) -> {
            // v 就是hello e 是异常，如果没异常就是null
            // 相当于回调，执行完了，执行什么操作，并且可以获取值
            System.out.println("-----------------执行完了--------------------");
            System.out.println(v);
        }).exceptionally(e -> {
            // 这一步就是出现异常了，要返回异常值
            System.out.println("----------异常了-----------");
            return null;
        });


        System.out.println("main线程去执行其他任务了");
        // 加这个的原因是，如果main线程执行完了，那么CompletableFuture就不会执行了
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();

    }

    @Test
    public void testCompletableFuture1() throws ExecutionException, InterruptedException, TimeoutException {
        // 解决Future get阻塞问题和idDone消耗cpu资源的问题
        // 需要能够上一步的结果集作为下一步的入参
        // 任务中最快的，最快的来了，其他的不要了了

        // runAsync  supplyAsync  有没有返回值

        // 执行无返回值的异步任务，也可以自己传入线程池
        CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());
        });

        // 执行有返回值的任务
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "hello";
        });
        // 还是会阻塞main线程
        // 获取返回值，get 和 join 的区别
        // join 不用try catch 异常
        // getNow 有就直接返回，没有就返回传入的值
        System.out.println(stringCompletableFuture.get());
        System.out.println(stringCompletableFuture.get(2, TimeUnit.SECONDS));
        System.out.println(stringCompletableFuture.join());
        System.out.println(stringCompletableFuture.getNow("XXX"));

        System.out.println("main线程去执行其他任务了");

    }


    @Test
    public void testFutureTask() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);

        long startTime = System.currentTimeMillis();

        FutureTask<String> futureTask1 = new FutureTask<>(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "1";
        });

        // 如果不get,main线程是不会管future是否执行完毕的，会直接往下执行
        // 缺点1：get方法会阻塞线程，建议放最后；
//        pool.submit(futureTask1).get();


        // 通过状态去判断是否计算完毕，计算完了才去取，不会造成线程阻塞，但是会造成cpu轮询
        // 缺点2：cpu轮询也不好，耗费cpu资源
        while (true) {
            if (futureTask1.isDone()) {
                pool.submit(futureTask1).get();
                break;
            } else {
                TimeUnit.MILLISECONDS.sleep(200);
            }
        }

        System.out.println("前面调用了get，所以必须获取到值才会继续忘下走");

        long time = System.currentTimeMillis() - startTime;

        pool.shutdown();
        System.out.println("耗时：" + time);
    }


    /**
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


}
