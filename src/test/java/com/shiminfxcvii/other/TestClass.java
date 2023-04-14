package com.shiminfxcvii.other;

import com.shiminfxcvii.controller.SearchRecordController;
import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.entity.SearchRecord;
import com.shiminfxcvii.enums.Sex;
import com.shiminfxcvii.repository.SearchRecordRepository;
import com.sun.istack.NotNull;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.shiminfxcvii.other.HttpStatusTest.BAD_REQUEST;
import static com.shiminfxcvii.other.TestClass.Color.RED;
import static com.shiminfxcvii.util.Constants.DATE_TIME;
import static java.awt.SystemColor.info;
import static java.lang.System.out;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpHeaders.CONTENT_RANGE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

//@SpringBootTest
public class TestClass {

    protected transient int modCount = 0;
    //    static {
//        out.println("static");
//    }
    HttpHeaders HTTP_HEADERS = new HttpHeaders();
    Employee employee = new Employee();
    transient Object[] elementData; // non-private to simplify nested class access
    private SearchRecordController searchRecordController;
    @Resource
    private SearchRecordRepository searchRecordRepository;
    private int size;

    private static native void registerNatives();

    /**
     * @author Zerox
     * @date 2018/12/4 15:59
     */

    public static void main(String[] args) {

//        System.out.println(testFunction(11, j -> j * 2 + 1));
//        System.out.println(testFunct(11, j -> j * 2 + 1));
    }

    //    {
//        out.println("not static");
//    }
    public static int testFunct(int i, Function<Integer, Integer> function) {

        return function.apply(i);
    }

    public static int testFunction(int i, Function<Integer, Integer> function) {

        return function.apply(i);
    }

    @Test
    public static Long testParam1(@org.jetbrains.annotations.NotNull Employee employee) {
        return employee.getId();
    }

    public static void error() {
        throw new AssertionError();
    }
    /**使用两个for循环实现List去重(有序)
     *
     * @param list
     * */
    /*public static List removeDuplicationBy2For(List<Integer> list) {
        for (int i=0;i<list.size();i++)
        {
            for (int j=i+1;j<list.size();j++)
            {
                if(list.get(i).equals(list.get(j))){
                    list.remove(j);
                }
            }
        }
        return list;
    }*/

    /**
     * 使用List集合contains方法循环遍历(有序)
     *
     * @param list
     */
    public static List removeDuplicationByContains(List<Integer> list) {
        List<Integer> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            boolean isContains = newList.contains(list.get(i));
            if (!isContains) {
                newList.add(list.get(i));
            }
        }
        list.clear();
        list.addAll(newList);
        return list;
    }

    /**
     * 使用HashSet实现List去重(无序)
     *
     * @param list
     */
    public static List removeDuplicationByHashSet(List<Integer> list) {
        HashSet set = new HashSet(list);
        //把List集合所有元素清空
        list.clear();
        //把HashSet对象添加至List集合
        list.addAll(set);
        return list;
    }

    /**
     * 使用LinkedHashSet实现List去重(有序)
     *
     * @param list
     */
    public static List removeDuplicationByLinkedHashSet(List<Integer> list) {
        LinkedHashSet set = new LinkedHashSet(list);
        //把List集合所有元素清空
        list.clear();
        //把HashSet对象添加至List集合
        list.addAll(set);
        return list;
    }

    /**
     * 使用TreeSet实现List去重(有序)
     *
     * @param list
     */
    public static List removeDuplicationByTreeSet(List<Integer> list) {
        TreeSet set = new TreeSet(list);
        //把List集合所有元素清空
        list.clear();
        //把HashSet对象添加至List集合
        list.addAll(set);
        return list;
    }

    /**
     * 使用java8新特性stream实现List去重(有序)
     *
     * @param list
     */
    public static List removeDuplicationByStream(List<Integer> list) {
//        List newList = list.stream().distinct().collect(Collectors.toList());
        List newList = list.stream().distinct().toList();
        return newList;
    }

    @Test
    public void test() {
        /*LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        hashSet.add("rtha");
        hashSet.add(";l,cfj");
        hashSet.add("/vkcjg");
        hashSet.add(".kc.j");
        hashSet.add("*kc,");
        hashSet.add("/565sj");
        hashSet.add("kuk%^^");
        hashSet.add("%%^xh");
        hashSet.add("hgjd");
        hashSet.add("%%^xh");
        hashSet.add("/uuu");
        hashSet.add("9");
        out.println(hashSet);
        out.println(hashSet.size());
        for (String value : hashSet) {
            out.println(value);
        }
        System.out.println("=============================");*/


        StopWatch sw = new StopWatch();
        sw.start("stream");

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            list.add("" + i);
            list.add("" + (i + 1));
            list.add("" + (i + 2));
        }
        list = list.stream().distinct().collect(Collectors.toList());

        sw.stop();
        System.out.println(sw.prettyPrint());
        System.out.println(sw.getTotalTimeSeconds());
        System.out.println(sw.getTotalTimeMillis());
        System.out.println(sw.getTotalTimeNanos());


        System.out.println("------------------------------------------------------------------------------------------");


        StopWatch sw2 = new StopWatch();
        sw2.start("LinkedHashSet");

        List<String> list22 = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            list22.add("" + i);
            list22.add("" + (i + 1));
            list22.add("" + (i + 2));
        }
        list22 = new LinkedHashSet<>(list22).stream().toList();

        sw2.stop();
        System.out.println(sw2.prettyPrint());
        System.out.println(sw2.getTotalTimeSeconds());
        System.out.println(sw2.getTotalTimeMillis());
        System.out.println(sw2.getTotalTimeNanos());


        System.out.println("------------------------------------------------------------------------------------------");


        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        List<Integer> list3 = new ArrayList<>();
        List<Integer> list4 = new ArrayList<>();
        List<Integer> list5 = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 1000000; i++) {
            int value = random.nextInt(500);
            list1.add(value);
            list2.add(value);
            list3.add(value);
            list4.add(value);
            list5.add(value);
        }
        long startTime;
        long endTime;
        startTime = System.currentTimeMillis();
        removeDuplicationByHashSet(list1);
        endTime = System.currentTimeMillis();
        System.out.println("使用HashSet实现List去重时间:" + (endTime - startTime) + "毫秒");
        startTime = System.currentTimeMillis();
        removeDuplicationByLinkedHashSet(list4);
        endTime = System.currentTimeMillis();
        System.out.println("使用LinkedHashSet实现List去重时间:" + (endTime - startTime) + "毫秒");
        startTime = System.currentTimeMillis();
        removeDuplicationByTreeSet(list2);
        endTime = System.currentTimeMillis();
        System.out.println("使用TreeSet实现List去重时间:" + (endTime - startTime) + "毫秒");
        startTime = System.currentTimeMillis();
        removeDuplicationByStream(list3);
        endTime = System.currentTimeMillis();
        System.out.println("使用java8新特性stream实现List去重:" + (endTime - startTime) + "毫秒");
//        startTime = System.currentTimeMillis();
//        removeDuplicationBy2For(list4);
//        endTime = System.currentTimeMillis();
//        System.out.println("使用两个for循环实现List去重:"+(endTime-startTime)+"毫秒");
        startTime = System.currentTimeMillis();
        removeDuplicationByContains(list5);
        endTime = System.currentTimeMillis();
        System.out.println("使用List集合contains方法循环遍历:" + (endTime - startTime) + "毫秒");

//        System.out.println(list);

        String[] recordNames = {};
        Set<String> set = new LinkedHashSet<>(List.of(recordNames));
        out.println(set);
        out.println(set.size());

//        List<String> list = new ArrayList<>();
//        List<String> list2 = new ArrayList<>();
//        list.add("a");
//        list.add("b");
//        list.add("c");
//        list.add("c");
//        list.add("g");
//        list2.add("a");
//        list2.add("e");
//        list2.add("e");
//        list2.add("g");
//        list2.add("g");
//        System.out.println("=============================");
//        for (String string : list) {
//            System.out.println(string);
//        }
//        System.out.println("=============================");
//        List<String> result = Stream.of(list, list2).flatMap(Collection::stream).distinct().toList();
//        out.println("result = " + result);
//        for (String s : result) {
//            out.println("s = " + s);
//        }
//        result.forEach(out::println);
//        System.out.println("=============================");
//        Stream<List<String>> list1 = Stream.of(list, list2);
//        Stream<String> stringStream = list1.flatMap(Collection::stream);
//        Stream<String> distinct = stringStream.distinct();
//        List<String> strings = distinct.toList();
//        strings.forEach(out::println);
//        System.out.println("=============================");
//        out.println(strings.subList(0, 3));

        /*TestVolatile testVolatile = new TestVolatile();
        testVolatile.changeStatus();
        testVolatile.run();
        Runnable r = () -> System.out.println("Run method");
        r.run();
        new Thread(() -> {
            for (int i = 0; i < 100; i++)
                out.println(i);
        }).start();
        for (int i = 0; i < 100; i++) {
            out.println("---------------------------------------"+i);
        }*/
    }

    @Test
    public void testList() {
        String str = "1234";
        out.println(str.substring(2));
        out.println(str.substring(2, 3));
        out.println(str.repeat(2));
        out.println(str.indent(2));
        out.println(str.charAt(2));
        out.println(11111111);
        out.println(str.concat("2"));
        out.println(str.substring(2, 3));
        out.println(str.subSequence(2, 3));
        switch (str) {
            case "0" -> out.println("0");
            case "1", "2" -> out.println("1 / 2");
        }
        String[] weekends = {"Friday", "Saturday", "Sunday"};
        assert weekends.length == 2 : "There are only 2 weekends in a week";
        System.out.println("There are " + weekends.length + "  weekends in a week");
        Object o = null;
//        out.println(o.equals(null));
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("1");
        out.println(list);
        out.println(isNull("fr"));
        out.println(hashCode());
        out.println(this.hashCode());
        out.println(super.hashCode());
        @NotNull
        List<String> strings = list.subList(2, list.size());
        out.println(strings);
    }

    public boolean remove(Object o) {
        final Object[] es = elementData;
        final int size = this.size;
        int i = 0;
        GOTO:
        {
            CONST:
            {
                if ("admin1".equals("username"))
                    break GOTO;
                else if ("admin2".equals("username"))
                    break CONST;

                return true;
            }
        }
        found:
        {
            if (o == null) {
                for (; i < size; i++)
                    if (es[i] == null)
                        break found;
            } else {
                for (; i < size; i++)
                    if (o.equals(es[i]))
                        break found;
            }
            return false;
        }
//        out.println(found);
        fastRemove(es, i);
        return true;
    }

    private void fastRemove(Object[] es, int i) {
        modCount++;
        final int newSize;
        if ((newSize = size - 1) > i)
            System.arraycopy(es, i + 1, es, i, newSize - i);
        es[size = newSize] = null;
    }

    private void responseEntity(@Nullable MultiValueMap<String, String> headers, @NonNull Object status) {
        Assert.notNull(status, "HttpStatus must not be null");
        out.println(status);
    }

    private void responseEntity(Object status) {
        Assert.notNull(status, "HttpStatus must not be null");
        out.println(status + "-------------------");
    }


    @Test
    public void testGetTopTenList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("" + i);
        }
        out.println(list);
    }

    @Test
    public void testZone() {
        Set<String> set1 = new LinkedHashSet<>();
        set1.add("1");
        set1.add("2");
        set1.add("2");
        set1.add("3");
        set1.add("h");
        set1.add("f");
        Set<String> set2 = new LinkedHashSet<>();
        set2.add("3");
        set2.add("4");
        set2.add("1");
        set2.add("4");
        set2.add("f");
        set2.add("s");
        set2.add("h");
        set2.add("5");
        Set<String> collect = Stream.of(set1, set2).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
        out.println(collect);
        int total = 0;
        try {
            for (; ; ) {
                System.out.println(total);
                Thread.sleep(1000);
                total++;
                if (total == 10) break;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        out.println(HttpMethod.values());
//        //invoking the of() method of the Stream class that returns a sequential ordered stream
//        Stream<String> st = Stream.of("San Jose", "Las Vegas", "Austin", "New York", "Denver", "Portland");
//        String[] str = {"San Jose", "Las Vegas", "Austin", "New York", "Denver", "Portland"};
//        //print the streams
//        st.forEach(out::println);
//        st.forEachOrdered(out::println);
//        st.max(out::println);
//        str.forEach(out::println);
    }

    @Test
    public void testParam2() {
//        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.PRC).withZone(ZoneId.of(SHORT_IDS.get("CTT")));
//        DateTimeFormatter DATE_TIME_FORMATTER2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.PRC).withZone(ZoneId.of("CTT", SHORT_IDS));
//        LocalDateTime now = LocalDateTime.now();
//        String dateTime = now.format(DATE_TIME_FORMATTER);
//
//        out.println(dateTime);
//        out.println(DATE_TIME_FORMATTER2);
//
//        out.println(SHORT_IDS);
//        out.println(SHORT_IDS.get("CTT"));
//        out.println(SHORT_IDS.get("CT"));
//        out.println(SHORT_IDS.values());
//
////        error();
//
//        HttpStatus o = HttpStatus.OK;
//        HttpStatus n = null;
//        out.println(o);
//
//        responseEntity(o);
//        responseEntity(n);
//        assert;

        HttpHeaders headers;
        headers = new HttpHeaders();
        out.println(headers);
//        headers.addIfAbsent(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.set(CONTENT_RANGE, ALL_VALUE);
//        headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        out.println(headers);
        out.println(headers.keySet());
        out.println(headers.values());
        headers.keySet().forEach(out::println);
        headers.values().forEach(out::println);
        out.println(headers.toSingleValueMap());
        out.println(headers.size());
        out.println(headers.get(CONTENT_TYPE));
        out.println(headers.containsKey(CONTENT_TYPE));
        out.println(HttpHeaders.ACCEPT);
        out.println(headers.getAccept());
        out.println(headers.getAcceptCharset());
        out.println(headers.getAcceptLanguage());
        out.println(headers.entrySet());
        out.println(headers.getAcceptLanguageAsLocales());
        out.println(headers.getAcceptPatch());
        out.println(headers.getAccessControlAllowCredentials());
        out.println(headers.getAccessControlAllowHeaders());
        out.println(headers.getAccessControlAllowMethods());
        out.println(headers.getAccessControlAllowOrigin());
        out.println(headers.getAccessControlExposeHeaders());
        out.println(headers.getAccessControlMaxAge());
        out.println(headers.getAccessControlRequestHeaders());
        out.println(headers.getAccessControlRequestMethod());
        out.println(headers.getAllow());
        out.println(headers.getCacheControl());
        out.println(headers.getConnection());
        out.println(headers.getContentDisposition());
        out.println(headers.getContentLanguage());
        out.println(headers.getContentLength());
        out.println(headers.getContentType());
        out.println(headers.getDate());
        out.println(headers.getETag());
        out.println(headers.getExpires());
        out.println(headers.getHost());
        out.println(headers.getIfMatch());
        out.println(headers.getIfModifiedSince());
        out.println(headers.getIfNoneMatch());
        out.println(headers.getIfUnmodifiedSince());
        out.println(headers.getLastModified());
        out.println(headers.getLocation());
        out.println(headers.getOrigin());
        out.println(headers.getPragma());
        out.println(headers.getRange());
        out.println(headers.getUpgrade());
        out.println(headers.getVary());
        out.println(headers.hashCode());
        out.println(headers.isEmpty());
        out.println(headers.keySet());
        out.println(headers.size());
        out.println(headers.values());

        /*out.println(employee);
        Employee employee = null;
        out.println(testParam1(employee));
        out.println(Pattern.matches("^[男女]$", "女"));
        out.println(null == employee);
        String s = testParam1(employee);
        out.println(s);
        out.println(UTF_8);
        out.println(UTF_8.toString() instanceof String);
        throw new NullPointerException("message");*/
    }

    @Test
    public void testEnum() {
        Employee employee = new Employee();
        employee.setEmployeeSex(3);
        Object o = new Object();

        if (0 == employee.getEmployeeSex() % 2) {
            out.println(Sex.Male.ordinal());
        } else {
            out.println(Sex.Female.ordinal());
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


//        SearchRecord searchRecord1 = new SearchRecord("1", "employeeName", "Jobs", "admin1", LocalDateTime.now());
//        SearchRecord searchRecord2 = new SearchRecord("2", "employeeName", "Anna", "admin3", LocalDateTime.now());
//        SearchRecord searchRecord3 = new SearchRecord("3", "employeeName", "Emma", "admin1", LocalDateTime.now());
//        List<SearchRecord> searchRecordList = new ArrayList<>();
//        searchRecordList.add(searchRecord1);
//        searchRecordList.add(searchRecord2);
//        searchRecordList.add(searchRecord3);
//        for (SearchRecord searchRecord : searchRecordList) {
//            out.println(searchRecord);
//        }

        Integer iii = null;
        Integer eiii = 0;

//        Byte recordNames = searchRecordRepository.countByUsernameAndSearchGroupByAndRecordName("admin1", "employeeName", "狄拉克");
//        out.println(recordNames);


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
        if (Pattern.matches("^[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}$", str1)) {
            out.println(1);
        }
        if (Pattern.matches("^[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}$", str2)) {
            out.println(1);
        }
        if (Pattern.matches("^[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}$", str3)) {
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
//        EmployeeController employeeController = new EmployeeController();
        out.println(com.shiminfxcvii.other.TestStatic.ENCODE);
        out.println(com.shiminfxcvii.other.TestStatic.STATUS);
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
        boolean matches = Pattern.matches("^[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}$", "37ea78ab-82ff-4bed-bf47-b76fddf28650");
        out.println(matches);
    }

    @Test
    public void test1() {

        // admin1/2/3
        String a1 = BCrypt.hashpw("WsmW%SVCmz8K*aT%tj", BCrypt.gensalt());
        System.out.println(a1);
        String a2 = BCrypt.hashpw("nDr6%RE&n3j45FF7$*%", BCrypt.gensalt());
        System.out.println(a2);
        String a3 = BCrypt.hashpw("M4SfvYt$JKf*O9PX5&SF", BCrypt.gensalt());
        System.out.println(a3);
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
        boolean matches = Pattern.matches("^[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}", "37ea78ab-82ff-4bed-bf47-b76fddf28650");
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
        out.println("".isEmpty());
        out.println("".isBlank());
        out.println(" ".isEmpty());
        out.println(" ".trim().isEmpty());

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
        out.println(com.shiminfxcvii.other.Constants.ZERO_int);
        out.println(com.shiminfxcvii.other.Constants.ZERO_int.value() instanceof Integer);
        out.println(Constants.PAGE_NUM.value() instanceof Integer);
        out.println(Constants.PAGE_NUM.value() instanceof String);
        out.println(Constants.X_CSRF_TOKEN);

//        out.println(RED);
//        out.println(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
//        out.println(HttpStatus.NOT_FOUND instanceof Object);
//        out.println(RED.getName() instanceof String);
    }

    private void startThread(Runnable task) {
        new Thread(task).start();
    }

    @Test
    public void testThread() {
        startThread(() -> System.out.println("线程任务执行！"));
        B aa = new B();
        aa.testA();
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

    @FunctionalInterface
    public interface MessageBuilder {
        String buildMessage();
    }

    public static class JavafinalizeExample1 {
        public static void main(String[] args) {
            JavafinalizeExample1 obj = new JavafinalizeExample1();
            System.out.println(obj.hashCode());
            obj = null;
            // calling garbage collector
            System.gc();
            System.out.println("end of garbage collection");

        }

        @Override
        protected void finalize() {
            System.out.println("finalize method called");
        }
    }

    public class Demo02LoggerLambda {
        private static void log(int level, MessageBuilder builder) {
            if (level == 1) {
                System.out.println(builder.buildMessage());// 实际上利用内部类 延迟的原理,代码不相关 无需进入到启动代理执行
            }
        }

        public static void main(String[] args) {
            String msgA = "Hello";
            String msgB = "World";
            String msgC = "Java";
            out.println(Arrays.toString(args));
            Object o = new Object();
            Object n = null;
            Assert.notNull(n, () -> {
                out.println("lambda ------------------");
                return msgA + msgB + msgC;
            });
//            ServletContext
            log(1, () -> {
                System.out.println("lambda 是否执行了");
                return msgA + msgB + msgC;
            });
        }
    }

    abstract class TestAbstract {
        private static final String STR = "h";

        public TestAbstract() {
            super();
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public class TestVolatile {
        int a = 1;
        boolean status = false;

        //状态切换为true
        public void changeStatus() {
            a = 2;                      // 1
            status = true;              // 2
        }

        // 若状态为true，则为running
        public void run() {
            if (status) {
                int b = a + 1;         // 3
                System.out.println(b); // 4
            }
        }

    }
}

class A {
    public void testA() {

    }
}

class B extends A {
    public void testB() {

    }
}