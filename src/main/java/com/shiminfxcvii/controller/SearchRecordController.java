package com.shiminfxcvii.controller;

import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.repository.EmployeeRepository;
import com.shiminfxcvii.repository.SearchRecordRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

import static com.shiminfxcvii.util.Constants.*;
import static com.shiminfxcvii.util.ListMethods.getListTopTenData;
import static com.shiminfxcvii.util.ListMethods.mergeTwoLists;
import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;
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

    @Resource
    private EmployeeRepository employeeRepository;

    @Resource
    private SearchRecordRepository searchRecordRepository;

    /**
     * 保存用户的搜索记录，数据由查找员工信息时一并传到后台，由 EmployeeController.findEmployeesBy() 方法调用<br>
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
     * @see com.shiminfxcvii.controller.EmployeeController#findEmployeesBy(Integer, Integer, Sort.Direction, String, Employee, Principal, Model)
     */
    public void saveSearchRecord(@NotNull Employee employee, @NotNull Principal user) throws IllegalAccessException {
        // 获取这个类的所有属性
        // 循环遍历所有的 fields
        for (Field field : employee.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (null != field.get(employee) && !Objects.equals(field.get(employee).toString(), "")) {
                String recordId = UUID.randomUUID().toString();
                SEARCH_RECORD.setRecordId(recordId);
                SEARCH_RECORD.setSearchGroupBy(field.getName());
                SEARCH_RECORD.setRecordName(field.get(employee).toString());
                SEARCH_RECORD.setUsername(user.getName());
                SEARCH_RECORD.setCreatedDate(LocalDateTime.now());
                searchRecordRepository.saveAndFlush(SEARCH_RECORD);
                if (searchRecordRepository.findById(recordId).isPresent()) {
                    LOGGER.info("搜索记录保存成功。");
                } else {
                    LOGGER.error("搜索记录保存失败。");
                }
                break;
            }
        }
    }

    /**
     * 接受删除搜索记录请求
     *
     * @param recordName 搜索记录 id
     * @param user       登录用户
     * @method deleteByRecordName
     * @author shiminfxcvii
     * @created 2022/5/7 21:11
     */
    @DeleteMapping(value = "deleteByRecordName", params = {SEARCH_GROUP_BY, RECORD_NAME}, headers = {CACHE_CONTROL, X_CSRF_TOKEN}, consumes = ALL_VALUE, produces = ALL_VALUE)
    public ResponseEntity<String> deleteByRecordName(@NotNull Principal user, @NotNull String searchGroupBy, @NotNull String recordName) {
        // 获取登录用户名
        String username = user.getName();
        // 返回信息
        String message;
        // 设置响应头信息
        HTTP_HEADERS.add(CONTENT_TYPE, ALL_VALUE);
        // 状态码
        HttpStatus status;

        // 需要删除的搜索记录不允许为空
        if (StringUtils.hasText(recordName)) {
            // 删除之前判断是否存在数据
            // 需要是属于该用户的搜索记录才允许被删除
            Byte count = searchRecordRepository.countByUsernameAndSearchGroupByAndRecordName(username, searchGroupBy, recordName);
            if (0 < count) {
                searchRecordRepository.deleteByUsernameAndSearchGroupByAndRecordName(username, searchGroupBy, recordName);
                // 检查是否成功删除
                count = searchRecordRepository.countByUsernameAndSearchGroupByAndRecordName(username, searchGroupBy, recordName);
                if (0 == count) {
                    message = "搜索记录删除成功。";
                    status = OK;
                } else {
                    message = "搜索记录删除失败，服务器出现故障。";
                    status = INTERNAL_SERVER_ERROR;
                }
            } else {
                message = "搜索记录删除失败，该搜索记录不存在。可能在此之前已经被删除。";
                status = BAD_REQUEST;
            }
        } else {
            message = "搜索记录删除失败，搜索记录名为空。";
            status = BAD_REQUEST;
        }

        return new ResponseEntity<>(message, HTTP_HEADERS, status);
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
    public ResponseEntity<List<String>> findRecordNamesBy(@NotNull Principal user, @NotNull String searchGroupBy, @Nullable String recordName) {
        // 获取登录用户名
        String username = user.getName();
        // 返回数据
        List<String> recordNames;
        // 设置响应头信息
        HTTP_HEADERS.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        // 有搜索内容
        if (StringUtils.hasText(recordName)) {
            // 查找此用户的搜索记录 ?%，recordName 可以为空
            List<String> thisRecordNamesOne = searchRecordRepository.findThisRecordNamesOne(username, searchGroupBy, recordName);
            // 如果有十条则返回，否则继续查找
            if (10 == thisRecordNamesOne.size())
                recordNames = thisRecordNamesOne;
            else {
                // 继续查找此用户的搜索记录 %?%，recordName 可以为空
                List<String> thisRecordNamesTwo = searchRecordRepository.findThisRecordNamesTwo(username, searchGroupBy, recordName);
                // 去重并合并，前 ?% ———— %?% 后
                List<String> thisRecordNamesThree = mergeTwoLists(thisRecordNamesOne, thisRecordNamesTwo);
                // 少于或者等于十条直接返回
                if (10 >= thisRecordNamesThree.size())
                    recordNames = thisRecordNamesThree;
                else
                    // 如果多于十条则只取前面十条数据
                    recordNames = getListTopTenData(thisRecordNamesThree);
            }
        } else
            // 没有搜索内容
            recordNames = searchRecordRepository.findThisRecordNamesOne(username, searchGroupBy, recordName);

        // 没有找到设置编码的方式，暂时不设置编码。似乎不需要设置
        // 使用 ResponseEntity 优点之一：不抛异常。而 HttpServletResponse 在 response.getWriter().write(message); 时会抛异常
        return new ResponseEntity<>(recordNames, HTTP_HEADERS, OK);
    }

    @GetMapping(value = "findAllEmployeesBy", params = "recordNames", headers = {CACHE_CONTROL, X_CSRF_TOKEN}, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> findAllEmployeesBy(@Nullable String recordNames, @NotNull Employee employee) throws IllegalAccessException {
        // 前台传过来的值
        List<String> recordNames1;
        // recordNames1 数据数量
        int size;

        // 把前台传过来的字符串转 List 数组
        if (StringUtils.hasText(recordNames))
            recordNames1 = Arrays.stream(recordNames.split(",")).toList();
        else
            recordNames1 = null;
        if (null != recordNames1)
            size = recordNames1.size();
        else
            size = 0;

        // 需要返回的值
        List<String> recordNames2;
        // 设置响应头信息
        HTTP_HEADERS.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        List<String> propertiesOfEmployeeList1 = new ArrayList<>();
        List<Employee> employeeList1 = employeeRepository.findAll(
                Example.of(
                        employee,
                        // 匹配所有字段的模糊查询并且忽略大小写，模糊匹配开头
                        ExampleMatcher.matching().withStringMatcher(STARTING)
                )
        );
        // 如果 employeeList1 有值
        if (0 < employeeList1.size()) {
            // 获取这个类的所有属性
            // 循环遍历所有的 fields
            for (Field field : employee.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (null != field.get(employee) && !Objects.equals(field.get(employee).toString(), "")) {
                    for (Employee employee1 : employeeList1)
                        propertiesOfEmployeeList1.add(field.get(employee1).toString());
                    break;
                }
            }
            // 先利用 Set 的特性使结果去重，然后两个 List 去重并合并
            if (null != recordNames1)
                propertiesOfEmployeeList1 = mergeTwoLists(recordNames1, new HashSet<>(propertiesOfEmployeeList1).stream().toList());
            else
                propertiesOfEmployeeList1 = new HashSet<>(propertiesOfEmployeeList1).stream().toList();
            // 截取出新的数据，去掉前台传过来的值（如果有
            propertiesOfEmployeeList1 = propertiesOfEmployeeList1.subList(size, propertiesOfEmployeeList1.size());
            // 如果数据数量满足所需要求
            if (10 - size == propertiesOfEmployeeList1.size())
                recordNames2 = propertiesOfEmployeeList1;
            else if (10 - size < propertiesOfEmployeeList1.size())
                recordNames2 = propertiesOfEmployeeList1.subList(0, 10 - size);
            else {
                List<String> arrayList = new ArrayList<>();
                List<Employee> repositoryAll = employeeRepository.findAll(
                        Example.of(
                                employee,
                                // 匹配所有字段的模糊查询并且忽略大小写，模糊匹配所有
                                ExampleMatcher.matching().withStringMatcher(CONTAINING)
                        )
                );
                // 获取这个类的所有属性
                // 循环遍历所有的 fields
                for (Field field : employee.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (null != field.get(employee) && !Objects.equals(field.get(employee).toString(), "")) {
                        for (Employee employee1 : repositoryAll)
                            arrayList.add(field.get(employee1).toString());
                        break;
                    }
                }
                // 先利用 Set 的特性使结果去重，然后两个 List 去重并合并
                List<String> mergeTwoLists = mergeTwoLists(propertiesOfEmployeeList1, new HashSet<>(arrayList).stream().toList());
                // 去重，不能与搜索记录有相同的数据
                List<String> twoLists;
                if (null != recordNames1)
                    twoLists = mergeTwoLists(recordNames1, mergeTwoLists);
                else
                    twoLists = mergeTwoLists;
                // 截取出新的数据，去掉前台传过来的值（如果有
                List<String> strings3 = twoLists.subList(size, twoLists.size());
                if (10 - size >= strings3.size())
                    // 直接返回结果
                    recordNames2 = getListTopTenData(strings3);
                else
                    recordNames2 = strings3.subList(0, 10 - size);
            }
            // 如果 employeeList1 没值
        } else {
            List<String> arrayList = new ArrayList<>();
            List<Employee> repositoryAll = employeeRepository.findAll(
                    Example.of(
                            employee,
                            // 匹配所有字段的模糊查询并且忽略大小写，模糊匹配所有
                            ExampleMatcher.matching().withStringMatcher(CONTAINING)
                    )
            );
            if (0 < repositoryAll.size()) {
                // 获取这个类的所有属性
                // 循环遍历所有的 fields
                for (Field field : employee.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (null != field.get(employee) && !Objects.equals(field.get(employee).toString(), "")) {
                        for (Employee employee1 : repositoryAll)
                            arrayList.add(field.get(employee1).toString());
                        break;
                    }
                }
                // 先利用 Set 的特性使结果去重，然后两个 List 去重并合并
                List<String> mergeTwoLists = mergeTwoLists(propertiesOfEmployeeList1, new HashSet<>(arrayList).stream().toList());
                // 去重，不能与搜索记录有相同的数据
                List<String> twoLists;
                if (null != recordNames1)
                    twoLists = mergeTwoLists(recordNames1, mergeTwoLists);
                else
                    twoLists = mergeTwoLists;
                // 截取出新的数据，去掉前台传过来的值（如果有
                List<String> strings3 = twoLists.subList(size, twoLists.size());
                if (10 - size >= strings3.size())
                    // 直接返回结果
                    recordNames2 = getListTopTenData(strings3);
                else
                    recordNames2 = strings3.subList(0, 10 - size);
                // 如果没有搜索到任何内容
            } else
                recordNames2 = new ArrayList<>();
        }
        // 没有找到设置编码的方式，暂时不设置编码。似乎不需要设置
        return new ResponseEntity<>(recordNames2, HTTP_HEADERS, OK);
    }

}