package com.shiminfxcvii.other;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ShiminFXCVII
 * @version 1.0
 * @description
 * @class Test2
 * @see
 * @since 2022/5/3 15:37 周二
 */
public class Test2 {
    private static String str;

    public static void main(String[] args) throws IOException {//程序主入口函数，抛出异常的声明
        /*while (true) {
            System.out.print("Please input:");
            Scanner s = new Scanner(System.in);//创建scanner，控制台会一直等待输入，直到敲回车结束
            Runtime r = Runtime.getRuntime();//调用脚本命令，打开所需程序
            int i = s.nextInt();//用户可自行定义i的值
            switch (i) {//指定switch语句表达式为变量i
                case 1 -> r.exec(new String[]{"**\\spotify.exe"});//当输入1时打开记事本
//跳出该函数
                case 2 -> r.exec("mspaint.exe");//当输入2时打开画图
//跳出该函数
                case 3 -> r.exec("C:\\啊哈C\\ahac.exe");//当输入3时打开啊哈c程序
//跳出该函数
                case 4 -> r.exec("D:\\Program Files\\Tencent\\qqmusic\\QQMusic.exe");//当输入4时打开qq音乐程序
//跳出该函数
                default -> {
                }//若无常量满足表达式，则执行default后的语句
            }
        }*/
        new Thread(() -> {
            try {
                str = "test1";
                Thread.sleep(10000);
                str = "test11";
                System.out.println("11 --------- " + str);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("12 --------- " + str);
        }).start();
        new Thread(() -> {
            str = "test2";
            System.out.println("22 --------- " + str);
        }).start();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("31 --------- " + str);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        System.out.println("4 --------- " + str);
    }

    @Test
    public void test1() {
        new Thread(() -> {
            try {
                str = "test1";
                Thread.sleep(10000);
                str = "test11";
                System.out.println(str);
                System.out.println(11);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(str);
            System.out.println(12);
        }).start();
        new Thread(() -> {
            str = "test2";
            System.out.println(str);
            System.out.println(22);
        }).start();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println(str);
                System.out.println(31);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        System.out.println(str);
        System.out.println(4);
    }
}

class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    public int size() {
        return 0;
    }

}

class ThreadTest extends Thread {

    private final String name;

    public ThreadTest(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        ThreadTest t1 = new ThreadTest("我是男一号");
        t1.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ThreadTest t2 = new ThreadTest("我是女一号");
        t2.start();
    }

    @Override
    public void run() {
        test();
    }

    public void test() {
        Double t = Math.random();
        synchronized (t) {
            System.out.println("进入同步区域 " + name);
            try {
                Thread.sleep(5000);  //睡5s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("离开同步区域 " + name);
        }
    }
}