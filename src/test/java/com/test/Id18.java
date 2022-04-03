package com.test;

import java.util.Date;
import java.util.UUID;

public class Id18 {
    //    public static void main(String[] args) {
//
//        //把uuid的时间戳转成可以识别的时间戳
////        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(138140162874696958L - 0x01b21dd213814000L, 0, ZoneOffset.ofHours(8));
////        System.out.println("localDateTime.now() = " + LocalDateTime.now());
//


    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        System.out.println(UUID.randomUUID());
        Date date = new Date();
        System.out.println(date);
    }

}