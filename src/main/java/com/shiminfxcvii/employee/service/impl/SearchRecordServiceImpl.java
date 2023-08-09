package com.shiminfxcvii.employee.service.impl;

import com.shiminfxcvii.employee.advice.SpringControllerAdvice;
import com.shiminfxcvii.employee.entity.Employee;
import com.shiminfxcvii.employee.entity.SearchRecord;
import com.shiminfxcvii.employee.model.cmd.EmployeeCmd;
import com.shiminfxcvii.employee.repository.EmployeeRepository;
import com.shiminfxcvii.employee.repository.SearchRecordRepository;
import com.shiminfxcvii.employee.service.SearchRecordService;
import com.shiminfxcvii.employee.util.Constants;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * SearchRecordServiceImpl
 *
 * @author ShiminFXCVII
 * @since 2022/10/3 23:10 周一
 */
@Service
public class SearchRecordServiceImpl implements SearchRecordService {

    private final SearchRecordRepository searchRecordRepository;
    private final EmployeeRepository employeeRepository;

    public SearchRecordServiceImpl(
            SearchRecordRepository searchRecordRepository,
            EmployeeRepository employeeRepository
    ) {
        this.searchRecordRepository = searchRecordRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * 保存用户的搜索记录，数据由查找员工信息时一并传到后台
     * 在这里使用的是 saveAndFlush() 而不是 save()，因为保存或者修改之后会立即查询数据库中该条数据
     * 如果使用 save() 可能会出现保存或者修改方法执行之后，立即查询数据库中该记录可能会出现不存在的情况
     * save()          将数据保存在内存中
     * saveAndFlush()  保存在内存中的同时同步到数据库
     * <p>
     * About the {@link org.springframework.transaction.annotation.Transactional}<br>
     * {@link org.springframework.data.jpa.repository.support.SimpleJpaRepository}
     * 中需要有 @Transactional 的 method 都有 @Transactional，但使用的却是默认的事务回滚的机制 ----
     * on {@link RuntimeException} and {@link Error}，<br>
     * 并且在 {@link SpringControllerAdvice} 中可以很好的定位抛出异常的地方
     * TODO: String body; HttpStatus status; 尝试使用原子
     *
     * @param user 登录用户
     * @param cmd  实体类某单一字段和属性
     * @return 响应信息
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @author ShiminFXCVII
     * @since 2022/10/3 20:41
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> saveSearchRecord(Principal user, EmployeeCmd cmd) throws IllegalAccessException {
        String body = "搜索记录保存失败，获取的登录用户名为空。";
        HttpStatus status = BAD_REQUEST;
        // 获取登录用户名
        String username = user.getName();
        // 设置标签
        hasText:
        if (StringUtils.hasText(username)) {
            // 获取这个类的所有属性
            for (Field field : cmd.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object object = field.get(cmd);
                // 在这里不能用 StringUtils.hasText(String.valueOf(obj)) 判断是否有值，因为 null 会被转换成 String -> "null"
                if (!ObjectUtils.isEmpty(object)) {
                    SearchRecord searchRecord = new SearchRecord();
                    searchRecord.setSearchGroupBy(field.getName());
                    searchRecord.setRecordName(String.valueOf(object));
                    searchRecordRepository.saveAndFlush(searchRecord);
                    // 检查是否保存成功
                    if (searchRecordRepository.existsById(Objects.requireNonNull(searchRecord.getId()))) {
                        body = "搜索记录保存成功。";
                        status = OK;
                    } else {
                        body = "搜索记录保存失败，数据没有成功保存到数据库。";
                        status = INTERNAL_SERVER_ERROR;
                    }
                    break hasText;
                }
            }
            body = "搜索记录保存失败，需要保存的值为空。";
        }

        // 设置响应头信息
        Constants.ALL.set(CONTENT_TYPE, ALL_VALUE);

        return new ResponseEntity<>(body, Constants.ALL, status);
    }

    /**
     * 接受删除搜索记录请求
     *
     * @param user         登录用户
     * @param searchRecord 接收前台传递的值
     * @return 响应信息
     * @author ShiminFXCVII
     * @since 2022/10/3 20:52
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> deleteByRecordName(Principal user, SearchRecord searchRecord) {
        String body;
        HttpStatus status;
        // 获取登录用户名
        String username = user.getName();

        if (StringUtils.hasText(username)) {
            // 获取搜索字段
            // 需要删除的搜索记录不允许为空
            if (StringUtils.hasText(searchRecord.getRecordName())) {
                // 删除之前判断是否存在数据
                // 需要是属于该用户的搜索记录才允许被删除
                List<SearchRecord> searchRecords = searchRecordRepository.findAll(Example.of(searchRecord));
                if (!CollectionUtils.isEmpty(searchRecords)) {
                    // 根据 id 执行批删除
                    searchRecordRepository.deleteAllInBatch(searchRecords);
                    // 检查是否成功删除
                    if (!searchRecordRepository.exists(Example.of(searchRecord))) {
                        body = "搜索记录删除成功。";
                        status = OK;
                    } else {
                        body = "搜索记录删除失败，服务器出现故障。";
                        status = INTERNAL_SERVER_ERROR;
                    }
                } else {
                    body = "搜索记录删除失败，该搜索记录不存在，可能在此之前已经被删除。";
                    status = BAD_REQUEST;
                }
            } else {
                body = "搜索记录删除失败，搜索记录名为空。";
                status = BAD_REQUEST;
            }
        } else {
            body = "搜索记录删除失败，登录用户名为空。";
            status = BAD_REQUEST;
        }

        // 设置响应头信息
        Constants.ALL.set(CONTENT_TYPE, ALL_VALUE);

        return new ResponseEntity<>(body, Constants.ALL, status);
    }

    /**
     * 根据条件和关键字查找搜索记录，用于前台搜索框的 autocomplete，自动完成提示
     *
     * @param user          登录用户
     * @param searchGroupBy 搜索字段
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
     * @param recordName    搜索关键字
     *                      将会根据该关键字执行 4 次查询，每两次搜索结果去重后整合为一个结果，最终返回给前台
     * @return 响应信息，并带有符合条件的搜索结果
     * @author ShiminFXCVII
     * @since 2022/10/3 21:07
     */
    @Override
    public ResponseEntity<Set<String>> findRecordNamesBy(Principal user, String searchGroupBy, String recordName) {
        // 获取登录用户名
        String username = user.getName();
        // 返回数据
        LinkedHashSet<String> recordNamesResponse;

        // 有搜索内容
        if (StringUtils.hasText(recordName)) {
            // 查找此用户的搜索记录 ?%，recordName 可以为空
            // Collections.synchronizedSet() 设为线程安全的 Set
            LinkedHashSet<String> thisRecordNames = searchRecordRepository.findThisRecordNames(username, searchGroupBy,
                    recordName + "%");
            // 如果有十条则返回，否则继续查找
            if (10 == thisRecordNames.size())
                recordNamesResponse = thisRecordNames;
            else {
                // 继续查找此用户的搜索记录 %?%，recordName 可以为空
                // 去重并合并，前 ?% ———— %?% 后
                thisRecordNames = Stream.of(thisRecordNames, searchRecordRepository
                                .findThisRecordNames(username, searchGroupBy, "%" + recordName + "%"))
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
            recordNamesResponse = searchRecordRepository.findThisRecordNames(username, searchGroupBy, recordName);

        // 设置响应头信息
        Constants.JSON.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        // 没有找到设置编码的方式，暂时不设置编码。似乎不需要设置
        // 使用 ResponseEntity 优点之一：不抛异常。而 HttpServletResponse 在 response.getWriter().write(message); 时会抛异常
        return new ResponseEntity<>(recordNamesResponse, Constants.JSON, OK);
    }

    /**
     * 用于前台搜索框 autocomplete，在当前用户的当前搜索记录不足 10 条时执行搜索，在 employee 表中搜索相关字段的信息
     *
     * @param recordNames 前台搜索框的值，可以为 null
     * @param employee    根据有值的字段搜索，用于接受前台传过来的值
     * @return 响应信息，并带有符合条件的搜索结果
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @author ShiminFXCVII
     * @since 2022/10/3 21:14
     */
    @Override
    public ResponseEntity<Set<String>> findAllPropertiesOfEmployeesBy(String[] recordNames, Employee employee)
            throws IllegalAccessException {
        // 前台传过来的搜搜记录，可能为空
        // 把前台传过来的字符串转成 List 数组
        LinkedHashSet<String> recordNamesRequest = new LinkedHashSet<>(List.of(recordNames));
        int size = recordNamesRequest.size();

        // 需要返回的值
        var propertiesResponse = getAllPropertiesOfEmployeesBy(employee, ExampleMatcher.StringMatcher.STARTING,
                recordNamesRequest, size, new LinkedHashSet<>());
        // 如果只根据 %? 搜索的结果不满足所需的数据量，则再根据 %?% 搜索一次
        if (10 - size > propertiesResponse.size())
            propertiesResponse = getAllPropertiesOfEmployeesBy(employee, ExampleMatcher.StringMatcher.CONTAINING,
                    recordNamesRequest, size, propertiesResponse);

        // 设置响应头信息
        Constants.JSON.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        return new ResponseEntity<>(propertiesResponse, Constants.JSON, OK);
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
     * @author ShiminFXCVII
     * @see #findAllPropertiesOfEmployeesBy
     * @since 2022/5/25 13:24
     */
    public LinkedHashSet<String> getAllPropertiesOfEmployeesBy(
            Employee employee,
            ExampleMatcher.StringMatcher stringMatcher,
            LinkedHashSet<String> recordNamesRequest,
            int size,
            LinkedHashSet<String> propertiesResponse)
            throws IllegalAccessException {
        LinkedHashSet<String> newPropertiesResponse = new LinkedHashSet<>();

        List<Employee> employeeList = employeeRepository.findAll(
                Example.of(
                        employee,
                        // 匹配所有字段的模糊查询并且忽略大小写，模糊匹配所有
                        ExampleMatcher.matchingAll().withStringMatcher(stringMatcher)
                )
        );

        // 如果有数据则将所需的值截取出来，如果没有数据则返回空数组
        if (!employeeList.isEmpty()) {
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
                newPropertiesResponse = Stream.of(recordNamesRequest, newPropertiesResponse)
                        .flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
                // 截取出新的数据，去掉前台传过来的值
                newPropertiesResponse = new LinkedHashSet<>(newPropertiesResponse.stream().toList()
                        .subList(size, newPropertiesResponse.size()));
            }
            // 如果之前已经根据 %? 查询过，则合并
            if (!CollectionUtils.isEmpty(propertiesResponse))
                newPropertiesResponse = Stream.of(propertiesResponse, newPropertiesResponse)
                        .flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
            // 如果结果大于所需的量需要去除多余的数据
            if (10 - size < newPropertiesResponse.size())
                newPropertiesResponse = new LinkedHashSet<>(newPropertiesResponse.stream().toList()
                        .subList(0, 10 - size));
        }

        return newPropertiesResponse;
    }

}