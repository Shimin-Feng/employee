package com.shiminfxcvii.controller;

import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.entity.SearchRecord;
import com.shiminfxcvii.repository.EmployeeRepository;
import com.shiminfxcvii.repository.SearchRecordRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.shiminfxcvii.util.Constants.*;
import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;
import static org.springframework.data.domain.ExampleMatcher.StringMatcher.STARTING;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description 对搜索记录的保存与查询
 * @class SearchRecordController
 * @created 2022/5/1 15:54 周日
 */
@Controller
@RequestMapping("searchRecord")
public class SearchRecordController {

    private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();
    private static final SearchRecord SEARCH_RECORD = new SearchRecord();
    private static HttpStatus status;
    private static String body;
    private static String username;
    @Resource
    private EmployeeRepository employeeRepository;
    @Resource
    private SearchRecordRepository searchRecordRepository;

    /**
     * 保存用户的搜索记录，数据由查找员工信息时一并传到后台，由 EmployeeController.findEmployeesBy() 方法调用。
     * 在这里使用的是 saveAndFlush() 而不是 save()，因为保存或者修改之后会立即查询数据库中该条数据
     * 如果使用 save() 可能会出现保存或者修改方法执行之后，立即查询数据库中该记录可能会出现不存在的情况
     * save()          将数据保存在内存中
     * saveAndFlush()  保存在内存中的同时同步到数据库
     *
     * @param user     String 登录用户
     * @param employee Employee 实体类某单一字段和属性
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @method saveSearchRecord
     * @author shiminfxcvii
     * @created 2022/4/30 11:11
     */
    @Transactional
    @PostMapping(value = "saveSearchRecord", headers = {CACHE_CONTROL, X_CSRF_TOKEN}, consumes = ALL_VALUE, produces = ALL_VALUE)
    public ResponseEntity<String> saveSearchRecord(@NotNull Principal user, @NotNull Employee employee) throws IllegalAccessException {
        // 获取登录用户名
        username = user.getName();
        if (StringUtils.hasText(username))
            // 获取这个类的所有属性
            // 循环遍历所有的 fields
            for (Field field : employee.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                final Object object = field.get(employee);
                // 在这里不能用 StringUtils.hasText(String.valueOf(obj)) 判断是否有值，因为 null 会被转换成 String -> "null"
                if (!ObjectUtils.isEmpty(object))
                    synchronized (SearchRecordController.class) {
                        SEARCH_RECORD.setRecordId(String.valueOf(UUID.randomUUID()));
                        SEARCH_RECORD.setSearchGroupBy(field.getName());
                        SEARCH_RECORD.setRecordName(String.valueOf(object));
                        SEARCH_RECORD.setUsername(username);
                        SEARCH_RECORD.setCreatedDate(LocalDateTime.now());
                        searchRecordRepository.saveAndFlush(SEARCH_RECORD);
                        // 下面判断并没有执行 sql
                        if (searchRecordRepository.findById(SEARCH_RECORD.getRecordId()).isPresent()) {
                            body = "搜索记录保存成功。";
                            status = OK;
                        } else {
                            body = "系统出错，搜索记录保存失败。";
                            status = INTERNAL_SERVER_ERROR;
                        }
                        break;
                    }
            }
        else {
            body = "搜索记录保存失败，获取的登录用户名为空。";
            status = BAD_REQUEST;
        }
        // 设置响应头信息
        HTTP_HEADERS.set(CONTENT_TYPE, ALL_VALUE);

        return new ResponseEntity<>(body, HTTP_HEADERS, status);
    }

    /**
     * 接受删除搜索记录请求
     *
     * @param user         登录用户
     * @param searchRecord 接收前台传递的值
     * @method deleteByRecordName
     * @author shiminfxcvii
     * @created 2022/5/7 21:11
     */
    @Transactional
    @DeleteMapping(value = "deleteByRecordName", params = {SEARCH_GROUP_BY, RECORD_NAME}, headers = {CACHE_CONTROL, X_CSRF_TOKEN}, consumes = ALL_VALUE, produces = ALL_VALUE)
    public ResponseEntity<String> deleteByRecordName(@NotNull Principal user, @NotNull SearchRecord searchRecord) {
        // 获取登录用户名
        username = user.getName();

        if (StringUtils.hasText(username))
            // 获取搜索字段
            // 需要删除的搜索记录不允许为空
            if (StringUtils.hasText(searchRecord.getRecordName())) {
                // 删除之前判断是否存在数据
                // 需要是属于该用户的搜索记录才允许被删除
                // 出于安全考虑，在此处获取 username，而不是从前台传过来
                searchRecord.setUsername(username);
                // Collections.synchronizedList() 设为线程安全的 List
                List<SearchRecord> searchRecords = Collections.synchronizedList(searchRecordRepository.findAll(Example.of(searchRecord)));
                if (0 < searchRecords.size())
                    synchronized (searchRecords) {
                        // 根据 id 执行批删除
                        searchRecordRepository.deleteAllInBatch(searchRecords);
                        // 检查是否成功删除
                        searchRecords = searchRecordRepository.findAll(Example.of(searchRecord));
                        if (0 == searchRecords.size()) {
                            body = "搜索记录删除成功。";
                            status = OK;
                        } else {
                            body = "搜索记录删除失败，服务器出现故障。";
                            status = INTERNAL_SERVER_ERROR;
                        }
                    }
                else {
                    body = "搜索记录删除失败，该搜索记录不存在。可能在此之前已经被删除。";
                    status = BAD_REQUEST;
                }
            } else {
                body = "搜索记录删除失败，搜索记录名为空。";
                status = BAD_REQUEST;
            }
        else {
            body = "搜索记录删除失败，登录用户名为空。";
            status = BAD_REQUEST;
        }
        // 设置响应头信息
        HTTP_HEADERS.set(CONTENT_TYPE, ALL_VALUE);

        return new ResponseEntity<>(body, HTTP_HEADERS, status);
    }

    /**
     * 根据条件和关键字查找搜索记录，用于前台搜索框的 autocomplete，自动完成提示
     *
     * @param user          Principal 登录用户
     * @param searchGroupBy String 搜索依据
     *                      <ul>可用字段
     *                       <li>employeeName             员工姓名</li>
     *                       <li>employeeSex              性别</li>
     *                       <li>employeeAge              年龄</li>
     *                       <li>employeeIdCard           身份证号码</li>
     *                       <li>employeeAddress          住址</li>
     *                       <li>employeePhoneNumber      电话号码</li>
     *                       <li>createdBy                添加者</li>
     *                       <li>createdDate              添加时间</li>
     *                       <li>lastModifiedDate         最后操作时间</li>
     *                      </ul>
     * @param recordName    String 搜索关键字
     *                      将会根据该关键字执行 4 次查询，每两次搜索结果去重后整合为一个结果，最终返回给前台
     * @return ResponseEntity<List < String>>
     * @method findRecordNamesBy
     * @author shiminfxcvii
     * @created 2022/4/29 11:34
     */
    @GetMapping(value = "findRecordNamesBy", params = {SEARCH_GROUP_BY, RECORD_NAME}, headers = {CACHE_CONTROL, X_CSRF_TOKEN}, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<String>> findRecordNamesBy(@NotNull Principal user, @NotNull String searchGroupBy, @Nullable String recordName) {
        // 获取登录用户名
        username = user.getName();
        // 返回数据
        Set<String> recordNamesResponse;

        // 有搜索内容
        if (StringUtils.hasText(recordName)) {
            // 查找此用户的搜索记录 ?%，recordName 可以为空
            // Collections.synchronizedSet() 设为线程安全的 Set
            Set<String> thisRecordNames = Collections.synchronizedSet(searchRecordRepository.findThisRecordNamesOne(username, searchGroupBy, recordName));
            // 如果有十条则返回，否则继续查找
            if (10 == thisRecordNames.size())
                recordNamesResponse = thisRecordNames;
            else
                synchronized (thisRecordNames) {
                    /*System.out.println("Thread.sleep(30000); start -------------------------------------------> " + recordName);
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Thread.sleep(30000); stop -------------------------------------------> " + recordName);*/
                    // 继续查找此用户的搜索记录 %?%，recordName 可以为空
                    // 去重并合并，前 ?% ———— %?% 后
                    thisRecordNames = Stream.of(thisRecordNames, searchRecordRepository.findThisRecordNamesTwo(username, searchGroupBy, recordName))
                            .flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
                    // 少于或者等于十条直接返回
                    if (10 >= thisRecordNames.size())
                        recordNamesResponse = thisRecordNames;
                    else
                        // 如果多于十条则只取前面十条数据
                        recordNamesResponse = new LinkedHashSet<>(thisRecordNames.stream().toList().subList(0, 10));
                }
        } else
            // 没有搜索内容
            recordNamesResponse = searchRecordRepository.findThisRecordNamesOne(username, searchGroupBy, recordName);
        // 设置响应头信息
        HTTP_HEADERS.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        // 没有找到设置编码的方式，暂时不设置编码。似乎不需要设置
        // 使用 ResponseEntity 优点之一：不抛异常。而 HttpServletResponse 在 response.getWriter().write(message); 时会抛异常
        return new ResponseEntity<>(recordNamesResponse, HTTP_HEADERS, OK);
    }

    /**
     * 用于前台搜索框 autocomplete，在当前用户的当前搜索记录不足 10 条时执行搜索，在 employee 表中搜索相关字段的信息
     *
     * @param recordNames 前台搜索框的值，可以为 null
     * @param employee    根据有值的字段搜索，用于接受前台传过来的值
     * @return ResponseEntity<List < String>>
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @method findAllPropertyOfEmployeesBy
     * @author shiminfxcvii
     * @created 2022/5/25 13:18
     * @see org.springframework.http.ResponseEntity
     */
    @GetMapping(value = "findAllPropertiesOfEmployeesBy", params = RECORD_NAMES, headers = {CACHE_CONTROL, X_CSRF_TOKEN}, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<String>> findAllPropertiesOfEmployeesBy(@Nullable String[] recordNames, @NotNull Employee employee) throws IllegalAccessException {
        // 前台传过来的搜搜记录，可能为空
        // 把前台传过来的字符串转成 List 数组
        final Set<String> recordNamesRequest = new LinkedHashSet<>(List.of(recordNames));
        final int size = recordNamesRequest.size();

        // 需要返回的值
        Set<String> propertiesResponse = Collections.synchronizedSet(getAllPropertiesOfEmployeesBy(employee, STARTING, recordNamesRequest, size, new LinkedHashSet<>()));
        // 如果只根据 %? 搜索的结果不满足所需的数据量，则再根据 %?% 搜索一次
        if (10 - size > propertiesResponse.size())
            synchronized (propertiesResponse) {
                propertiesResponse = getAllPropertiesOfEmployeesBy(employee, CONTAINING, recordNamesRequest, size, propertiesResponse);
            }
        // 设置响应头信息
        HTTP_HEADERS.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        // 没有找到设置编码的方式，暂时不设置编码。似乎不需要设置
        return new ResponseEntity<>(propertiesResponse, HTTP_HEADERS, OK);
    }

    /**
     * 执行搜索操作
     *
     * @param employee           根据有值的字段搜索，用于接受前台传过来的值
     * @param stringMatcher      搜索规则
     * @param recordNamesRequest 已有的数据，前台传过来的值
     * @param size               已有数据量（前台传过来的值的数量）
     * @param propertiesResponse 如果长度大于 0，则数据是通过 %? 搜索之后的值
     * @return 经过筛选后的值
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @method getRecordNames
     * @author shiminfxcvii
     * @created 2022/5/25 13:24
     * @see #findAllPropertiesOfEmployeesBy
     */
    public Set<String> getAllPropertiesOfEmployeesBy(@NotNull Employee employee,
                                                     @NotNull ExampleMatcher.StringMatcher stringMatcher,
                                                     @Nullable Set<String> recordNamesRequest,
                                                     int size,
                                                     @Nullable Set<String> propertiesResponse) throws IllegalAccessException {
        Set<String> newPropertiesResponse = Collections.synchronizedSet(new LinkedHashSet<>());

        final List<Employee> employeeList = employeeRepository.findAll(
                Example.of(
                        employee,
                        // 匹配所有字段的模糊查询并且忽略大小写，模糊匹配所有
                        ExampleMatcher.matchingAll().withStringMatcher(stringMatcher)
                )
        );
        // 如果有数据则将所需的值截取出来，如果没有数据则返回空数组
        if (0 < employeeList.size())
            synchronized (newPropertiesResponse) {
                // 获取 employee 的所有属性
                // 循环遍历所有的 fields
                for (Field field : employee.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    // 在这里不能用 StringUtils.hasText(String.valueOf(obj)) 判断是否有值，因为 null 会被转换成 String
                    if (!ObjectUtils.isEmpty(field.get(employee))) {
                        for (Employee e : employeeList)
                            newPropertiesResponse.add(String.valueOf(field.get(e)));
                        break;
                    }
                }
                // 如果前台传过来的值不为空
                if (!CollectionUtils.isEmpty(recordNamesRequest)) {
                    // 先利用 stream().distinct() 的使结果去重，然后两个 List 去重并合并。不能与搜索记录有相同的数据
                    newPropertiesResponse = Stream.of(recordNamesRequest, newPropertiesResponse).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
                    // 截取出新的数据，去掉前台传过来的值
                    newPropertiesResponse = new LinkedHashSet<>(newPropertiesResponse.stream().toList().subList(size, newPropertiesResponse.size()));
                }
                // 如果之前已经根据 %? 查询过，则合并
                if (!CollectionUtils.isEmpty(propertiesResponse))
                    newPropertiesResponse = Stream.of(propertiesResponse, newPropertiesResponse).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
                // 如果结果大于所需的量需要去除多余的数据
                if (10 - size < newPropertiesResponse.size())
                    newPropertiesResponse = new LinkedHashSet<>(newPropertiesResponse.stream().toList().subList(0, 10 - size));
            }

        return newPropertiesResponse;
    }

}