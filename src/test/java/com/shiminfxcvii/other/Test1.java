package com.shiminfxcvii.other;

import javax.swing.*;
import java.util.Scanner;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description
 * @class Test1
 * @see
 * @since 2022/5/3 15:33 周二
 */
public class Test1 {
    public static void main(String[] args) {//程序主函数
        while (true) {//定义死循环
            System.out.print("Please input:");//提示输入
            Scanner s = new Scanner(System.in);//创建scanner，控制台会一直等待输入，直到敲回车结束
            String str = s.nextLine();//将用户的输入转换为字符串形式
            if ("ByeBye".equals(str)) {//if语句的条件判断用户输入是否为ByeBye
                System.out.print("The process is over");//输出进程已结束
                System.exit(0);//关闭进程
            } else {
                //使用消息提示框输出信息
                JOptionPane.showMessageDialog(null, "You input is " + str, str, JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
}