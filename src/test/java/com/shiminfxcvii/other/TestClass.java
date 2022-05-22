package com.shiminfxcvii.other;

import com.shiminfxcvii.controller.EmployeeController;
import com.shiminfxcvii.controller.SearchRecordController;
import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.entity.SearchRecord;
import com.shiminfxcvii.repository.SearchRecordRepository;
import com.shiminfxcvii.util.Sex;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.util.StopWatch;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.shiminfxcvii.other.HttpStatusTest.BAD_REQUEST;
import static com.shiminfxcvii.other.TestClass.Color.RED;
import static com.shiminfxcvii.util.Constants.DATE_TIME;
import static java.awt.SystemColor.info;
import static java.lang.System.out;
import static java.nio.charset.StandardCharsets.UTF_8;

//@SpringBootTest
public class TestClass {

//    static {
//        out.println("static");
//    }

    private SearchRecordController searchRecordController;
    @Resource
    private SearchRecordRepository searchRecordRepository;

//    {
//        out.println("not static");
//    }


    @Test
    public void testList() {
        Object o = null;
        out.println(o.equals(null));
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("1");
        out.println(list);
        List<String> strings = list.subList(2, list.size());
        out.println(strings);
    }

    @Test
    public void testParam2() {
        Employee employee = new Employee();
        out.println(null == employee);
        String s = testParam1(employee);
        out.println(s);
        out.println(UTF_8);
        out.println(UTF_8.toString() instanceof String);
    }


    @Test
    public String testParam1(Employee employee) {
        return employee.getEmployeeId();
    }

    @Test
    public void testEnum() {
        Employee employee = new Employee();
        employee.setEmployeeSex("3");
        Object o = new Object();

        if (0 == Integer.parseInt(employee.getEmployeeSex()) % 2) {
            out.println(Sex.Male.getNumber());
        } else {
            out.println(Sex.Female.getNumber());
        }

        /*out.println(StandardCharsets.UTF_8);


        String s = null;
        String s2 = "";
        String s3 = "    ";
        String s4 = "1";
        String s5 = " 4 1  4 ";
        out.println(org.springframework.util.StringUtils.hasLength(s));
        out.println(org.springframework.util.StringUtils.hasText(s));
        out.println(org.springframework.util.StringUtils.hasLength(s2));
        out.println(org.springframework.util.StringUtils.hasText(s2));
        out.println(org.springframework.util.StringUtils.hasLength(s3.trim()));
        out.println(org.springframework.util.StringUtils.hasText(s3));
        out.println(org.springframework.util.StringUtils.hasText(s5.trim()));
        out.println(org.springframework.util.StringUtils.trimWhitespace(Objects.requireNonNull(s4)));
        out.println(org.springframework.util.StringUtils.trimAllWhitespace(s));

        out.println(HttpStatus.NOT_FOUND);
        out.println(HttpStatus.BAD_REQUEST);*/

        /*try {
            int i = 10/0;
        } catch (Exception e) {
            out.println(e.getMessage());
            out.println(e.getCause());
            out.println(e.getLocalizedMessage());
            out.println(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }*/
    }

    @Test
    public void testIterable() {
        StopWatch sw = new StopWatch();
        sw.start("校验耗时");


        SearchRecord searchRecord1 = new SearchRecord("1", "employeeName", "Jobs", "admin1", LocalDateTime.now());
        SearchRecord searchRecord2 = new SearchRecord("2", "employeeName", "Anna", "admin3", LocalDateTime.now());
        SearchRecord searchRecord3 = new SearchRecord("3", "employeeName", "Emma", "admin1", LocalDateTime.now());
        List<SearchRecord> searchRecordList = new ArrayList<>();
        searchRecordList.add(searchRecord1);
        searchRecordList.add(searchRecord2);
        searchRecordList.add(searchRecord3);
        for (SearchRecord searchRecord : searchRecordList) {
            out.println(searchRecord);
        }

        Integer iii = null;
        Integer eiii = 0;

        Byte recordNames = searchRecordRepository.countByUsernameAndSearchGroupByAndRecordName("admin1", "employeeName", "狄拉克");
        out.println(recordNames);


        sw.stop();
        System.out.println(sw.prettyPrint());
    }

    @Test
    public void testMap() {
        List<Map<String, String>> mapList = new ArrayList<>();
        HashMap<String, String> maps1 = new HashMap<>();
        Map<String, String> maps2 = new HashMap<>();
        maps1.put("Jobs", "22");
//        maps1.put("Anna", "22");
        maps2.put("JobS", "22");
        mapList.add(maps1);
        mapList.add(maps2);
        out.println(mapList);
//        Set<String> strings1 = maps1.keySet();
//        Set<String> strings2 = maps2.keySet();
//        out.println(strings1);
//        out.println(strings2);
//        out.println(strings1.equals(strings2));
        out.println(maps1.keySet());
        out.println(maps2.keySet());
        out.println(maps1.keySet().equals(maps2.keySet()));
        out.println(maps1.entrySet());
        out.println(maps1.isEmpty());
        out.println(maps1.size());
        out.println(maps1.values());
        out.println(maps2.values());
        out.println(maps1.values().iterator());
        out.println(maps2.values().iterator());
        out.println(maps1.values().iterator().next());
        out.println(maps2.values().iterator().next());
        out.println(maps1.values().iterator().next().equals(maps2.values().iterator().next()));
    }

    @Test
    public void testPattern() {
        String str1 = null;
        String str2 = "";
        String str3 = "d";

        // java.lang.NullPointerException: Cannot invoke "java.lang.CharSequence.length()" because "this.text" is null
        if (Pattern.matches("^\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}$", str1)) {
            out.println(1);
        }
        if (Pattern.matches("^\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}$", str2)) {
            out.println(1);
        }
        if (Pattern.matches("^\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}$", str3)) {
            out.println(1);
        }
    }

    @Test
    public void testInfo() {
        out.println(info.getGreen());
        out.println(info.brighter());
        out.println(info.getAlpha());
        out.println(info.getBlue());
        out.println(info.getColorSpace());
        out.println(info.darker());
    }

    @Test
    public void testStatic() {
        EmployeeController employeeController = new EmployeeController();
        out.println(TestStatic.ENCODE);
        out.println(TestStatic.STATUS);
    }

    @Test
    public void testEntity() {
        SearchRecord searchRecord = new SearchRecord();
        SearchRecord searchRecord2 = searchRecord;
        out.println(searchRecord.hashCode());
        out.println(searchRecord2.hashCode());
    }

    @Test
    public void testStringUtils() {
        String str = "";
        String str1 = "你好";
        String str2 = str1;
        out.println(StringUtils.isEmpty(str));
        out.println("简化.isEmpty(str)");
        out.println(str1);
        out.println(str2);
        out.println(StringUtils.isEmpty(str1));
        out.println(org.junit.platform.commons.util.StringUtils.isNotBlank(""));
        out.println(org.junit.platform.commons.util.StringUtils.isNotBlank("j"));
    }

    @Test
    public void testBoolean() {
        boolean flag = Boolean.parseBoolean(null);
        Comparator<String> caseInsensitiveOrder = String.CASE_INSENSITIVE_ORDER;
        out.println(caseInsensitiveOrder);
        String s = String.CASE_INSENSITIVE_ORDER.toString();
        out.println(s);
    }

    @Test
    public void testEquals() {
        int a = 10;
        int b = 11;
        out.println((a++ == b) && (++a == b));
    }

    @Test
    public void testIDCard() {
        String idCard = "51152919970104501X";
        String i = Integer.parseInt(String.valueOf(idCard.charAt(16))) % 2 == 0 ? "女" : "男";
        out.println(i);
    }

    @Test
    public void test9() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String format = now.format(dateTimeFormatter);
        out.println(format);

        out.println(now.getYear());
        out.println(now.getMonthValue());
        out.println(now.getDayOfMonth());
        int yearNow = now.getYear();
        int monthNow = now.getMonthValue();
        int dayNow = now.getDayOfMonth();


        String idCard = "51152919970104501x";
        out.println(Integer.parseInt(idCard.substring(6, 10)));
        out.println(Integer.parseInt(idCard.substring(10, 12)));
        out.println(Integer.parseInt(idCard.substring(12, 14)));

        int yearBirth = Integer.parseInt(idCard.substring(6, 10));
        int monthBirth = Integer.parseInt(idCard.substring(10, 12));
        int dayBirth = Integer.parseInt(idCard.substring(12, 14));


        String age = String.valueOf(dayNow - dayBirth < 0
                ? monthNow - 1 - monthBirth < 0 ? yearNow - 1 - yearBirth : yearNow - yearBirth
                : monthNow - monthBirth < 0 ? yearNow - 1 - yearBirth : yearNow - yearBirth);
        out.println(age);

        int iii = 343;
        Integer uuu = iii;

        out.println(idCard.toUpperCase());
    }

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

        out.println("Color.RED  : " + RED.ordinal());
        out.println("Color.RED  : " + RED.name());
        out.println("Color.RED  : " + RED.ordinal());
        out.println("Color.RED  : " + RED.hashCode());
        out.println("Color.RED  : " + RED.compareTo(Color.GREEN));
        out.println("Color.RED  : " + Color.GREEN.compareTo(RED));
        System.out.println("Color.GREEN: " + Color.GREEN.ordinal());

        System.out.println("Color.BLUE : " + Color.class);
        System.out.println("Color.BLUE : " + Color.valueOf(String.valueOf(RED)));
        System.out.println("Color.BLUE : " + Color.valueOf(Color.class, "RED"));
        System.out.println("Color.BLUE : " + Arrays.toString(Color.values()));


    }

    @Test
    public void testEnum1() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.US);
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
        String dateTime = now.format(dateTimeFormatter);
        String dateTime1 = now.format(dateTimeFormatter1);
        String dateTime2 = now.format(dateTimeFormatter2);

        out.println(dateTime);
        out.println(dateTime1);
        out.println(dateTime2);
//        HttpStatus notFound = HttpStatus.NOT_FOUND;
//        out.println(TestEnum.ZERO_INTEGER);

        for (HttpStatus value : HttpStatus.values()) {
            out.println(value);
        }
        out.println(BAD_REQUEST.value());
        out.println(BAD_REQUEST);
        out.println(DATE_TIME);
        out.println(DATE_TIME);
        out.println(Constants.ZERO_int);
        out.println(Constants.ZERO_int.value() instanceof Integer);
        out.println(Constants.PAGE_NUM.value() instanceof Integer);
        out.println(Constants.PAGE_NUM.value() instanceof String);
        out.println(Constants.X_CSRF_TOKEN);

//        out.println(RED);
//        out.println(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
//        out.println(HttpStatus.NOT_FOUND instanceof Object);
//        out.println(RED.getName() instanceof String);
    }

    public enum TestEnum1 {

//        NETWORK_AUTHENTICATION_REQUIRED(511, HttpStatus.Series.SERVER_ERROR, "Network Authentication Required");
    }

    public enum Color {
        RED("red"), GREEN("green"), BLUE("blue");
        private final String name;

        Color(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}