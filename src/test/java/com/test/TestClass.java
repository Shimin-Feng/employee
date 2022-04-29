package com.test;

import com.example.entity.Employee;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.System.out;

public class TestClass {

    @Test
    public void test8() {
        Employee employee = new Employee();
        out.println(employee);
    }

    @Test
    public void test7() {
        out.println(
                0 == 0 && 1 == 6 && 2 == 2 || 3 == 3
        );
    }

    @Test
    public void test6() {
        String str = "";
        out.println(str.isEmpty());
        String str1 = null;
        out.println("" == str1);
        String str2 = "";
        out.println("".equals(str2));
    }

    @Test
    public void test5() {
        UUID uuid = UUID.randomUUID();
        out.println(uuid);
        String s = uuid.toString();
        out.println(s);
    }

    @Test
    public void test4() {
        boolean matches = Pattern.matches("^\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}$", "37ea78ab-82ff-4bed-bf47-b76fddf28650");
        out.println(matches);
    }


    @Test
    public void test1() {

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


        out.println("借记卡");


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
        out.println(objects);
        for (int i = objects.size() - 1; i > -1; i--) {
            if (objects.get(i).toString().matches("\\W")) {
                str2.append(new StringBuilder(objects.get(i).toString()).reverse());
            } else {
                str2.append(new StringBuilder(objects.get(i).toString()).reverse()).append(" ");
            }
        }
        out.println(str2.toString().trim());

    }

    @Test
    public void test2() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "a");
        map.put("2", "b");
        out.println(map);
        out.println(map.size());
        Integer i = 1;
        int c = 1;
        out.println(i.equals(c));
        out.println(i == c);
        String sssn = "";
        out.println(!Objects.equals(sssn, ""));
    }

    @Test
    public void test3() {
        boolean matches = Pattern.matches("^\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}", "37ea78ab-82ff-4bed-bf47-b76fddf28650");
        out.println(matches);


        //创建一个list集合并对其赋值
        List<String> list = new ArrayList<>();
        out.println(list);
        out.println(list.size());
        Stream.iterate(1, s -> ++s).limit(20).forEach(s -> list.add(s + ""));
        out.println(list);
        List<String> allRecordNameFour = new ArrayList<>();
        int i = 0;
        for (String recordName2 : list) {
            out.println(recordName2);
            if (i < 10) {
                allRecordNameFour.add(recordName2);
                i++;
            } else {
                break;
            }
        }
        out.println(allRecordNameFour);

    }
}