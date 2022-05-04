package com.shiminfxcvii.other;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description
 * @class Test2
 * @created 2022/5/3 15:37 周二
 * @see
 */
public class Test2 {
    public static void main(String[] args) throws IOException {//程序主入口函数，抛出异常的声明
        while (true) {
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
        }
    }
}