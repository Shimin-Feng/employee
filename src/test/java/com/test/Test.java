package com.test;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {

//        // admin1/2/3
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


        /*Date date = new Date();
        System.out.println(date);
        System.out.println("借记卡");*/


        /*String str = "I love this world";
        String[] splitWords = str.split("\s");
        System.out.println(Arrays.toString(splitWords));
        StringBuilder words = new StringBuilder();
        for (int i = splitWords.length - 1; i > -1; i--) {
            words.append(splitWords[i]).append(" ");
        }
        System.out.println(words.toString().trim());*/


        /*String str = "I love this world, and I love my family.";
        String regExp = "\\w+|[,.]";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(str);
        ArrayList<Object> objects = new ArrayList<>();
        StringBuilder str2 = new StringBuilder();
        while (matcher.find()) {
            objects.add(matcher.group());
        }
        System.out.println(objects);
        for (int i = objects.size() - 1; i > -1; i--) {
            if (objects.get(i).toString().matches("\\W")) {
                str2.append(objects.get(i));
            } else {
                str2.append(objects.get(i)).append(" ");
            }
        }
        System.out.println(str2.toString().trim());*/


        String str = "I love this world, and I love my family.";
        String regExp = "\\w+|[,.]";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(str);
        ArrayList<Object> objects = new ArrayList<>();
        StringBuilder str2 = new StringBuilder();
        while (matcher.find()) {
            objects.add(matcher.group());
        }
        System.out.println(objects);
        for (int i = objects.size() - 1; i > -1; i--) {
            if (objects.get(i).toString().matches("\\W")) {
                str2.append(new StringBuilder(objects.get(i).toString()).reverse());
            } else {
                str2.append(new StringBuilder(objects.get(i).toString()).reverse()).append(" ");
            }
        }
        System.out.println(str2.toString().trim());

    }
}