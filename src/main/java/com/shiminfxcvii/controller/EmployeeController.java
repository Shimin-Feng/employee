package com.shiminfxcvii.controller;

import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.repository.EmployeeRepository;
import com.shiminfxcvii.util.Sex;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.shiminfxcvii.util.Constants.*;
import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * 操作员工信息 ———— CRUD
 *
 * @author shiminfxcvii
 * @since 2022/5/1 14:50
 */
@Controller
@RequestMapping("employee")
public class EmployeeController {
    private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();
    private static HttpStatus status;
    private static String body;
    @Resource
    private EmployeeRepository employeeRepository;
    @Resource
    private OperationLogController operationLogController;

    /**
     * 当进入员工管理页面之前会立即执行一次查询所有员工，默认按照添加时间升序排列，每页十条数据
     * 设置两个排序字段是为了防止在翻页时出现数据重复
     *
     * @param model Model 页面模型
     * @return "employee" 返回查询后的整个页面
     * @method employee
     * @author shiminfxcvii
     * @since 2022/4/29 10:32
     */
    @GetMapping
    public String employee(Model model) {
        Page<Employee> employees = employeeRepository.findAll(PageRequest.of(ZERO_INTEGER, TEN_INTEGER, Sort.by(CREATED_DATE, EMPLOYEE_ID)));
        // 数据库存储的性别是 0 和 1，但是需要使页面正常显示
        for (Employee employee : employees)
            employee.setEmployeeSex(Objects.requireNonNull(Sex.resolveByNumber(employee.getEmployeeSex())).getGender());
        model.addAttribute(EMPLOYEES, employees);
        return "employee";
    }

    /**
     * 该方法有两个作用，添加和修改员工信息
     * 在这里使用的是 saveAndFlush() 而不是 save()，因为保存或者修改之后会立即查询数据库中该条数据
     * 如果使用 save() 可能会出现保存或者修改方法执行之后，立即查询数据库中该记录可能会出现不存在的情况
     * save()          将数据保存在内存中
     * saveAndFlush()  保存在内存中的同时同步到数据库
     * PATCH 只更新部分字段
     *
     * @param user     Principal 登录用户
     * @param employee Employee 前台传过来的需要添加或者修改的员工信息，根据是否存在 employeeId 判断该请求为添加还是修改
     *                 <ul>
     *                  <li>
     *                  如果是添加请求，将会在执行添加 sql 之后立即查询数据库是否存在该员工信息
     *                  存在则返回 status 200 ”添加成功“，否则返回 status 500 ”添加失败“
     *                  </li>
     *                  <li>
     *                  如果是修改请求，将会在执行该请求之前查询该修改请求的所有字段是否与修改前相同，不相同则执行修改请求，
     *                  在执行修改 sql 之后会立即再次查询该请求所包含数据是否成功修改到书库
     *                  成功则返回 status 200 ”修改成功“，否则返回 400 "修改失败，因为员工信息没有任何改变"
     *                  </li>
     *                 </ul>
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @method saveOrUpdateEmployee
     * @author shiminfxcvii
     * @since 2022/4/29 10:59
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(
            value = "saveOrUpdateEmployee",
            method = {RequestMethod.POST, RequestMethod.PATCH},
            params = {EMPLOYEE_NAME, EMPLOYEE_ID_CARD, EMPLOYEE_ADDRESS, EMPLOYEE_PHONE_NUMBER, EMPLOYEE_ID},
            headers = {CACHE_CONTROL, X_CSRF_TOKEN},
            consumes = ALL_VALUE,
            produces = ALL_VALUE
    )
    public synchronized ResponseEntity<String> saveOrUpdateEmployee(@NotNull Principal user, @NotNull Employee employee) throws IllegalAccessException {
        // Get dateTime now
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DATE_TIME_FORMATTER);

        // Get sex
        // 数据库存储的性别是 0 和 1
        String number = String.valueOf(Integer.parseInt(String.valueOf(employee.getEmployeeIdCard().charAt(16))) % 2);

        // Get age
        // now
        int yearNow = now.getYear(),
                monthNow = now.getMonthValue(),
                dayNow = now.getDayOfMonth(),
                // birth
                yearBirth = Integer.parseInt(employee.getEmployeeIdCard().substring(6, 10)),
                monthBirth = Integer.parseInt(employee.getEmployeeIdCard().substring(10, 12)),
                dayBirth = Integer.parseInt(employee.getEmployeeIdCard().substring(12, 14));
        // age
        String age = String.valueOf(
                dayNow - dayBirth < 0
                        ? monthNow - 1 - monthBirth < 0 ? yearNow - 1 - yearBirth : yearNow - yearBirth
                        : monthNow - monthBirth < 0 ? yearNow - 1 - yearBirth : yearNow - yearBirth
        );

        // idCard
        String idCard = employee.getEmployeeIdCard().toUpperCase();

        String employeeId = employee.getEmployeeId();

        if (!StringUtils.hasText(employeeId)) {
            // save
            // 设置值
            employee.setEmployeeId(String.valueOf(UUID.randomUUID()));
            employee.setEmployeeSex(number);
            employee.setEmployeeAge(age);
            // 转大写处理后的值
            employee.setEmployeeIdCard(idCard);
            employee.setCreatedBy(user.getName());
            employee.setCreatedDate(dateTime);
            employee.setLastModifiedDate(dateTime);
            // 保存到数据库
            employeeRepository.saveAndFlush(employee);
            // 检查是否成功保存到数据库
            if (employeeRepository.findById(employee.getEmployeeId()).isPresent()) {
                // 保存操作日志
                operationLogController.saveOperationLog(INSERT, employee, user);
                body = "添加成功。";
                status = OK;
            } else {
                body = "添加失败，服务器错误，员工信息未保存进数据库。";
                status = INTERNAL_SERVER_ERROR;
            }
        } else {
            // update
            Optional<Employee> employee1 = employeeRepository.findById(employeeId);
            // 判断是否存在该员工
            if (employee1.isPresent()) {
                // 获取对象
                Employee employee2 = employee1.get();
                // 修改之前比较被修改对象的值与前台传递过来的值是否相同，不相同则执行修改操作
                if (!employee.equals(employee2)) {
                    // 设置值
                    employee.setEmployeeSex(number);
                    employee.setEmployeeAge(age);
                    // 转大写处理后的值
                    employee.setEmployeeIdCard(idCard);
                    // 因为需要保存操作日志，所以这里还是要设置值
                    employee.setCreatedBy(employee2.getCreatedBy());
                    employee.setCreatedDate(employee2.getCreatedDate());
                    employee.setLastModifiedDate(dateTime);
                    // 执行修改操作
                    employeeRepository.saveAndFlush(employee);
                    // 执行修改之后再次查询该员工属性
                    Optional<Employee> employee3 = employeeRepository.findById(employeeId);
                    // 如果数据存在则获取对象
                    // 修改之后比较被修改对象的值与前台传递过来的值是否相同，判断该数据是否成功修改
                    if (employee3.isPresent() && employee.equals(employee3.get())) {
                        // 保存操作日志
                        operationLogController.saveOperationLog(UPDATE, employee, user);
                        body = "修改成功。";
                        status = OK;
                    } else {
                        body = "服务器出现故障，修改失败，员工信息未被成功修改进数据库。";
                        status = INTERNAL_SERVER_ERROR;
                    }
                } else {
                    body = "修改失败，因为员工信息没有任何改变。";
                    status = BAD_REQUEST;
                }
            } else {
                body = "修改失败，该员工不存在。";
                status = BAD_REQUEST;
            }
        }
        // 设置响应头信息
        HTTP_HEADERS.set(CONTENT_TYPE, ALL_VALUE);

        return new ResponseEntity<>(body, HTTP_HEADERS, status);
    }

    /**
     * 根据员工 ID 删除员工信息
     *
     * @param user       Principal 获取登录用户信息
     * @param employeeId String 前台传过来的 employeeId
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @method deleteEmployeeById
     * @author shiminfxcvii
     * @since 2022/4/29 11:20
     */
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(
            value = "deleteEmployeeById",
            params = EMPLOYEE_ID,
            headers = {CACHE_CONTROL, X_CSRF_TOKEN},
            consumes = ALL_VALUE,
            produces = ALL_VALUE
    )
    public synchronized ResponseEntity<String> deleteEmployeeById(@NotNull Principal user, @NotNull String employeeId) throws IllegalAccessException {
        if (StringUtils.hasText(employeeId))
            if (Pattern.matches("^\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}$", employeeId)) {
                Optional<Employee> employee = employeeRepository.findById(employeeId);
                if (employee.isPresent()) {
                    employeeRepository.deleteById(employeeId);
                    if (employeeRepository.findById(employeeId).isEmpty()) {
                        // 保存操作日志
                        operationLogController.saveOperationLog(DELETE, employee.get(), user);
                        body = "删除成功。";
                        status = OK;
                    } else {
                        body = "服务器出现故障，删除失败，员工信息还存在于数据库。";
                        status = INTERNAL_SERVER_ERROR;
                    }
                } else {
                    body = "删除失败，因为数据库没有该员工信息。";
                    status = BAD_REQUEST;
                }
            } else {
                body = "删除失败，非法 ID。ID 格式不正确。";
                status = BAD_REQUEST;
            }
        else {
            body = "删除失败，ID 为空。";
            status = BAD_REQUEST;
        }
        // 设置响应头信息
        HTTP_HEADERS.set(CONTENT_TYPE, ALL_VALUE);

        return new ResponseEntity<>(body, HTTP_HEADERS, status);
    }

    /**
     * 根据条件和关键字搜索员工信息<br>
     * 除了首次进入员工管理页面前调用 employee()<br>
     * 其他查询请求全部改为进入该方法<br>
     * 就算前台不传值过来，@RequestBody Employee employee 也不会为空<br>
     * null == employee //false
     *
     * @param pageNum   Integer 返回该值所有页数数据，默认第 1 页
     * @param pageSize  Integer 该页数据显示条数，默认 10 条数据
     * @param direction Sort.Direction 排序规则，ASC 升序，DESC 降序
     * @param property  String 根据该字段排序，默认 createdDate 添加时间
     *                  <ul>可用字段
     *                   <li>employeeName             员工姓名</li>
     *                   <li>employeeSex              性别</li>
     *                   <li>employeeAge              年龄</li>
     *                   <li>employeeIdCard           身份证号码</li>
     *                   <li>employeeAddress          住址</li>
     *                   <li>employeePhoneNumber      电话号码</li>
     *                   <li>createdBy                添加者</li>
     *                   <li>createdDate              添加时间</li>
     *                   <li>lastModifiedDate         最后操作时间</li>
     *                  </ul>
     * @param employee  Employee 根据员工的某一个字段和值进行搜索
     * @param model     Model 页面模型
     * @return "employee" 返回查询后的整个页面
     * @method findEmployeesBy
     * @author shiminfxcvii
     * @since 2022/4/29 11:50
     */
    @GetMapping(
            value = "findEmployeesBy",
            params = {PAGE_NUM, PAGE_SIZE, DIRECTION, PROPERTY},
            headers = {CACHE_CONTROL, X_CSRF_TOKEN},
            consumes = ALL_VALUE,
            produces = TEXT_HTML_VALUE
    )
    public String findEmployeesBy(@RequestParam(value = PAGE_NUM, defaultValue = ZERO) Integer pageNum,
                                  @RequestParam(value = PAGE_SIZE, defaultValue = TEN) Integer pageSize,
                                  @RequestParam(value = DIRECTION, defaultValue = ASC) Sort.Direction direction,
                                  @RequestParam(value = PROPERTY, defaultValue = CREATED_DATE) String property,
                                  @NotNull Employee employee,
                                  Model model) {
        // 如果是根据性别搜索，先判断值是否有效，再根据实际性别解析成对应的数字
        if (StringUtils.hasText(employee.getEmployeeSex()) && Pattern.matches("^[女男]$", employee.getEmployeeSex()))
            employee.setEmployeeSex(Objects.requireNonNull(Sex.resolveByGender(employee.getEmployeeSex())).getNumber());

        Page<Employee> employees = employeeRepository.findAll(
                Example.of(
                        employee,
                        // 匹配所有字段的模糊查询并且忽略大小写
                        ExampleMatcher.matchingAll().withStringMatcher(CONTAINING)
                ),
                PageRequest.of(pageNum, pageSize, Sort.by(direction, property, EMPLOYEE_ID))
        );

        // 数据库存储的性别是 0 和 1，但是需要使页面正常显示
        for (Employee employee1 : employees)
            employee1.setEmployeeSex(Objects.requireNonNull(Sex.resolveByNumber(employee1.getEmployeeSex())).getGender());

        model.addAttribute(EMPLOYEES, employees);

        return "employee";
    }

}