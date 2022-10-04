package com.shiminfxcvii.service.impl;

import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.enums.Sex;
import com.shiminfxcvii.model.cmd.EmployeeCmd;
import com.shiminfxcvii.model.dto.EmployeeDTO;
import com.shiminfxcvii.model.query.EmployeeQuery;
import com.shiminfxcvii.repository.EmployeeRepository;
import com.shiminfxcvii.service.EmployeeService;
import com.shiminfxcvii.service.OperationLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.shiminfxcvii.util.Constants.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.ALL_VALUE;

/**
 * EmployeeServiceImpl
 *
 * @author shiminfxcvii
 * @since 2022/10/3 15:09 周一
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final OperationLogService operationLogService;

    @Lazy
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               OperationLogService operationLogService
    ) {
        this.employeeRepository = employeeRepository;
        this.operationLogService = operationLogService;
    }

    /**
     * 当进入员工管理页面之前会立即执行一次查询所有员工，默认按照添加时间升序排列，每页十条数据
     *
     * @param model Model 页面模型对象
     * @author ShiminFXCVII
     * @since 2022/10/3 15:14
     */
    @Override
    public void employee(Model model) {
        Page<Employee> employees = employeeRepository.findAll(PageRequest.of(ZERO_INTEGER, TEN_INTEGER,
                Sort.by(CREATED_DATE, EMPLOYEE_ID)));
        // 数据库存储的性别是 0 和 1，但是需要使页面正常显示
        employees.map(employee -> {
            EmployeeDTO dto = new EmployeeDTO();
            BeanUtils.copyProperties(employee, dto);
            dto.setEmployeeSex(Sex.getGenderByOrdinal(employee.getEmployeeSex()));
            return dto;
        });
        model.addAttribute(EMPLOYEES, employees);
    }

    /**
     * 该方法有两个作用，添加和修改员工信息
     * 在这里使用的是 saveAndFlush() 而不是 save()，因为保存或者修改之后会立即查询数据库中该条数据
     * 如果使用 save() 可能会出现保存或者修改方法执行之后，立即查询数据库中该记录可能会出现不存在的情况
     * save()          将数据保存在内存中
     * saveAndFlush()  保存在内存中的同时同步到数据库
     * PATCH 只更新部分字段
     *
     * @param user    登录用户
     * @param cmd     前台传过来的需要添加或者修改的员工信息，根据是否存在 employeeId 判断该请求为添加还是修改
     *                <ul>
     *                 <li>
     *                 如果是添加请求，将会在执行添加 sql 之后立即查询数据库是否存在该员工信息
     *                 存在则返回 status 200 ”添加成功“，否则返回 status 500 ”添加失败“
     *                 </li>
     *                 <li>
     *                 如果是修改请求，将会在执行该请求之前查询该修改请求的所有字段是否与修改前相同，不相同则执行修改请求，
     *                 在执行修改 sql 之后会立即再次查询该请求所包含数据是否成功修改到书库
     *                 成功则返回 status 200 ”修改成功“，否则返回 400 "修改失败，因为员工信息没有任何改变"
     *                 </li>
     *                </ul>
     * @param request 请求信息
     * @return 响应信息
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @author ShiminFXCVII
     * @since 2022/10/3 15:14
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> saveOrUpdateEmployee(Principal user,
                                                       EmployeeCmd cmd,
                                                       RequestEntity<Employee> request) throws IllegalAccessException {
        int sum = 0;

        byte[] bytes = cmd.getEmployeeIdCard().getBytes();

        for (int i = 0; i < bytes.length; i++) {
            sum += bytes[i] * WEIGHT[i];
        }
        if (bytes[18] != VALIDATE[sum % 11]) {
            // 设置响应头信息
            HTTP_HEADERS.set(CONTENT_TYPE, ALL_VALUE);

            return new ResponseEntity<>("身份证号码不正确", HTTP_HEADERS, BAD_REQUEST);
        }

        HttpStatus status;
        String body;

        // Get dateTime now
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DATE_TIME_FORMATTER);

        // Get sex
        // 数据库存储的性别是 0 和 1
        int sex = Integer.parseInt(cmd.getEmployeeIdCard().substring(16, 17)) % 2;

        // Get age
        // now
        int yearNow = now.getYear(),
                monthNow = now.getMonthValue(),
                dayNow = now.getDayOfMonth(),
                // birth
                yearBirth = Integer.parseInt(cmd.getEmployeeIdCard().substring(6, 10)),
                monthBirth = Integer.parseInt(cmd.getEmployeeIdCard().substring(10, 12)),
                dayBirth = Integer.parseInt(cmd.getEmployeeIdCard().substring(12, 14));
        // age
        int age =
                dayNow - dayBirth < 0
                        ? monthNow - 1 - monthBirth < 0 ? yearNow - 1 - yearBirth : yearNow - yearBirth
                        : monthNow - monthBirth < 0 ? yearNow - 1 - yearBirth : yearNow - yearBirth;

        // idCard
        String idCard = cmd.getEmployeeIdCard().toUpperCase();

        // get the method of the request
        if (HttpMethod.POST.equals(request.getMethod())) {
            // save
            // 设置值
            Employee employee = new Employee();
            employee.setEmployeeId(String.valueOf(UUID.randomUUID()));
            employee.setEmployeeSex(sex);
            employee.setEmployeeAge(age);
            // 转大写处理后的值
            employee.setEmployeeIdCard(idCard);
            employee.setCreatedBy(user.getName());
            employee.setCreatedDate(dateTime);
            employee.setLastModifiedDate(dateTime);
            // 保存到数据库
            employeeRepository.saveAndFlush(employee);
            // 检查是否成功保存到数据库
            if (employeeRepository.existsById(employee.getEmployeeId())) {
                // 保存操作日志
                operationLogService.saveOperationLog(INSERT, employee, user);
                body = "添加成功。";
                status = OK;
            } else {
                body = "添加失败，服务器错误，员工信息未保存进数据库。";
                status = INTERNAL_SERVER_ERROR;
            }
        } else {
            // update
            Optional<Employee> employeeOptional = employeeRepository.findById(cmd.getEmployeeId());
            // 判断是否存在该员工
            if (employeeOptional.isPresent()) {
                // 获取对象
                Employee employee1 = employeeOptional.get();
                // 修改之前比较被修改对象的值与前台传递过来的值是否相同，不相同则执行修改操作
                if (Objects.equals(employee1.getEmployeeName(), cmd.getEmployeeName()) &&
                        Objects.equals(employee1.getEmployeeIdCard(), cmd.getEmployeeIdCard()) &&
                        Objects.equals(employee1.getEmployeeAddress(), cmd.getEmployeeAddress()) &&
                        Objects.equals(employee1.getEmployeePhoneNumber(), cmd.getEmployeePhoneNumber())) {
                    // 设置值，先判断值是否改变
                    if (!employee1.getEmployeeName().equals(cmd.getEmployeeName()))
                        employee1.setEmployeeName(cmd.getEmployeeName());
                    if (!employee1.getEmployeeSex().equals(sex))
                        employee1.setEmployeeSex(sex);
                    if (!employee1.getEmployeeAge().equals(age))
                        employee1.setEmployeeAge(age);
                    if (!employee1.getEmployeeIdCard().equals(idCard))
                        employee1.setEmployeeIdCard(idCard);
                    if (!employee1.getEmployeeAddress().equals(cmd.getEmployeeAddress()))
                        employee1.setEmployeeAddress(cmd.getEmployeeAddress());
                    if (!employee1.getEmployeePhoneNumber().equals(cmd.getEmployeePhoneNumber()))
                        employee1.setEmployeePhoneNumber(cmd.getEmployeePhoneNumber());
                    employee1.setLastModifiedDate(dateTime);
                    // 执行修改操作
                    employeeRepository.saveAndFlush(employee1);
                    // 因为保存之后立马查询不会执行，所以这里不再做查询操作
                    // 保存操作日志
                    operationLogService.saveOperationLog(UPDATE, employee1, user);
                    body = "修改成功。";
                    status = OK;
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
     * @param user 获取登录用户信息
     * @param id   前台传过来的 employeeId
     * @return 响应信息
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @author ShiminFXCVII
     * @since 2022/10/3 15:36
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> deleteEmployeeById(Principal user, String id) throws IllegalAccessException {
        HttpStatus status;
        String body;

        if (StringUtils.hasText(id)) {
            if (Pattern.matches("^[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}$",
                    id)) {
                Optional<Employee> employee = employeeRepository.findById(id);
                if (employee.isPresent()) {
                    employeeRepository.deleteById(id);
                    if (employeeRepository.findById(id).isEmpty()) {
                        // 保存操作日志
                        operationLogService.saveOperationLog(DELETE, employee.get(), user);
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
        } else {
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
     * @param pageNum   返回该值所有页数数据，默认第 1 页
     * @param pageSize  该页数据显示条数，默认 10 条数据
     * @param direction 排序规则，ASC 升序，DESC 降序
     * @param property  根据该字段排序，默认 createdDate 添加时间
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
     * @param query     根据员工的某一个字段和值进行搜索
     * @param model     页面模型
     * @author ShiminFXCVII
     * @since 2022/10/3 16:31
     */
    @Override
    public void findEmployeesBy(Integer pageNum,
                                Integer pageSize,
                                Sort.Direction direction,
                                String property,
                                EmployeeQuery query,
                                Model model) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(query, employee);
        // 如果是根据性别搜索，先判断值是否有效，再根据实际性别解析成对应的数字
        String employeeSex = query.getEmployeeSex();
        if (StringUtils.hasText(employeeSex) && Pattern.matches("^[女男]$", employeeSex))
            employee.setEmployeeSex(Sex.getOrdinalByGender(employeeSex));

        Page<Employee> employees = employeeRepository.findAll(
                Example.of(
                        employee,
                        // 匹配所有字段的模糊查询并且忽略大小写
                        ExampleMatcher.matchingAll().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                ),
                PageRequest.of(pageNum, pageSize, Sort.by(direction, property, EMPLOYEE_ID))
        );

        // 数据库存储的性别是 0 和 1，但是需要使页面正常显示
        employees.map(employee1 -> {
            EmployeeDTO dto = new EmployeeDTO();
            BeanUtils.copyProperties(employee1, dto);
            dto.setEmployeeSex(Sex.getGenderByOrdinal(employee1.getEmployeeSex()));
            return dto;
        });
        model.addAttribute(EMPLOYEES, employees);
    }

}
