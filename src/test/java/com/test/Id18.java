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

//        String a1 = BCrypt.hashpw("WsmW%SVCmz8K*aT%tj", BCrypt.gensalt());
//        System.out.println(a1);
//        String a2 = BCrypt.hashpw("nDr6%RE&n3j45FF7$*%", BCrypt.gensalt());
//        System.out.println(a2);
//        String a3 = BCrypt.hashpw("M4SfvYt$JKf*O9PX5&SF", BCrypt.gensalt());
//        System.out.println(a3);
//
//        // user1/2/3
//        String e1 = BCrypt.hashpw("jzRSMe%2wiR&poH7mfV*cr", BCrypt.gensalt());
//        System.out.println(e1);
//        String e2 = BCrypt.hashpw("Nt&YNt8bM*M6nsSM&4YM3", BCrypt.gensalt());
//        System.out.println(e2);
//        String e3 = BCrypt.hashpw("Bg2nbRM*n7LyDD$LmSX", BCrypt.gensalt());
//        System.out.println(e3);
    }

}