package com.test;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Id18 {
    //    public static void main(String[] args) {
//
//        //把uuid的时间戳转成可以识别的时间戳
////        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(138140162874696958L - 0x01b21dd213814000L, 0, ZoneOffset.ofHours(8));
////        System.out.println("localDateTime.now() = " + LocalDateTime.now());
//
//    }
    int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};    //十七位数字本体码权重
    char[] validate = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};    //mod11,对应校验码字符值

    public char getValidateCode(@NotNull String id17) {
        int sum = 0;
        int mode;
        for (int i = 0; i < id17.length(); i++) {
            sum += Integer.parseInt(String.valueOf(id17.charAt(i))) * weight[i];
        }
        mode = sum % 11;
        return validate[mode];
    }

    public static void main(String[] args) {
        Id18 test = new Id18();
        System.out.println("该身份证验证码：" + test.getValidateCode("11022519640302612"));    //该身份证校验码：3
    }

}