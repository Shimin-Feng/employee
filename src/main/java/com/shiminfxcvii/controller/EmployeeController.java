package com.shiminfxcvii.controller;

import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.repository.EmployeeRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description 操作员工信息 ———— CRUD
 * @class EmployeeController
 * @created 2022/5/1 14:50
 */
@Controller
public class EmployeeController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    // 编码格式，不设置编码前台就无法解析汉字
    private static final String ENCODE = "UTF-8";
    // 状态码
    private int status;
    // 返回信息
    private String message;
    @Resource
    private EmployeeRepository employeeRepository;
    @Resource
    private OperationLogController operationLogController;
    @Resource
    private SearchRecordController searchRecordController;

    // TODO: <input> 如何解决在使用中文输入时的错误？
    // TODO: 学习新一代 thymeleaf-extras-spring security6 的使用方法
    // TODO: 统计图
    // TODO: 数据库根据时间自动调整年龄
    // TODO: 实现使用拼音也能搜索
    // TODO: ExampleMatcher 匹配 SearchRecord 搜索（考虑）
    // TODO: 后续可以添加用户管理界面，管理请假界面
    // TODO: 迁移数据库之后 employee_management employee 编码为 utf8mb4，所以某些查询会出现问题
    // TODO: 如此频繁地查询数据库是否真的有必要？
    // TODO: 原生 js forEach 的用法
    // TODO: 根据本项目目前的情况，js 中修改员工信息弹窗事件委托暂时先放在 tbody
    // TODO: js 加分号，换成 var
    // TODO: @NotNull 换成自己判断
    // TODO: 再次尝试将 js 查找员工的两个方法写进一个方法里
    // TODO: 设置页面最小不可变 400px

    /**
     * 在登录之前访问任何资源都将跳转到自定义登录界面
     *
     * @return "login" login 页面
     * @method login
     * @author shiminfxcvii
     * @created 2022/4/29 16:46
     */
    @RequestMapping("login")
    public String login() {
        return "login";
    }

    /**
     * 接受请求跳转到 index 页面
     *
     * @return "index" index 页面
     * @method index
     * @author shiminfxcvii
     * @created 2022/4/29 16:33
     */
    @RequestMapping("index")
    public String index() {
        return "index";
    }

    /*@RequestMapping("logout")
    public String logout() {
        return "logout";
    }*/

    /*@RequestMapping("login-failed")
    public String loginFailed() {
        return "login-failed";
    }*/

    // TODO: 为什么点退出会来的 timeout 页面？
    /*@RequestMapping("timeout")
    public String timeout() {
        return "timeout";
    }*/

    /**
     * 当进入员工管理页面之前会立即执行一次查询所有员工，默认按照添加时间升序排列，每页十条数据
     * 设置两个排序字段是为了防止在翻页时出现数据重复
     *
     * @param model Model 页面模型
     * @return "employee" 返回查询后的整个页面
     * @method employee
     * @author shiminfxcvii
     * @created 2022/4/29 10:32
     */
    @RequestMapping("employee")
    public String employee(@NotNull Model model) {
        model.addAttribute(
                "employees",
                employeeRepository.findAll(PageRequest.of(0, 10, Sort.by("createdDate", "employeeId"))));
        return "employee";
    }

    /**
     * 该方法有两个作用，添加和修改员工信息
     * 在这里使用的是 saveAndFlush() 而不是 save()，因为保存或者修改之后会立即查询数据库中该条数据
     * 如果使用 save() 可能会出现保存或者修改方法执行之后，立即查询数据库中该记录可能会出现不存在的情况
     * save()          将数据保存在内存中
     * saveAndFlush()  保存在内存中的同时同步到数据库
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
     * @param response HttpServletResponse 需要返回的状态和信息
     * @throws IOException 写入响应信息异常
     * @method saveOrUpdateEmployee
     * @author shiminfxcvii
     * @created 2022/4/29 10:59
     */
    @RequestMapping("employee/saveOrUpdateEmployee")
    public void saveOrUpdateEmployee(@NotNull Principal user, @NotNull Employee employee, HttpServletResponse response) throws IOException {
        // Get dateTime now
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DATE_TIME_FORMATTER);

        // Get sex
        String sex = Integer.parseInt(String.valueOf(employee.getEmployeeIdCard().charAt(16))) % 2 == 0 ? "女" : "男";

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

        if (null == employee.getEmployeeId() || Objects.equals(employee.getEmployeeId(), "")) {
            // save
            // 设置值
            employee.setEmployeeId(UUID.randomUUID().toString());
            employee.setEmployeeSex(sex);
            employee.setEmployeeAge(age);
            employee.setEmployeeIdCard(idCard);
            employee.setCreatedBy(user.getName());
            employee.setCreatedDate(dateTime);
            employee.setLastModifiedDate(dateTime);
            // 保存到数据库
            employeeRepository.saveAndFlush(employee);
            // 检查是否成功保存到数据库
            if (employeeRepository.findById(employee.getEmployeeId()).isPresent()) {
                // 保存操作日志
                operationLogController.saveOperationLog("INSERT", employee, user);
                status = 200;
                message = "添加成功。";
            } else {
                status = 500;
                message = "添加失败，服务器错误，员工信息未保存进数据库。";
            }
        } else {
            // update
            Optional<Employee> employee1 = employeeRepository.findById(employee.getEmployeeId());
            // 判断是否存在该员工
            if (employee1.isPresent()) {
                // 获取对象
                Employee employee2 = employee1.get();
                // 修改之前比较被修改对象的值与前台传递过来的值是否相同，不相同则执行修改操作
                if (!employee.equals(employee2)) {
                    // 设置值
                    employee.setEmployeeSex(sex);
                    employee.setEmployeeAge(age);
                    employee.setEmployeeIdCard(idCard);
                    employee.setCreatedBy(employee2.getCreatedBy());
                    employee.setCreatedDate(employee2.getCreatedDate());
                    employee.setLastModifiedDate(dateTime);
                    // 执行修改操作
                    employeeRepository.saveAndFlush(employee);
                    // 执行修改之后再次查询该员工属性
                    Optional<Employee> employee3 = employeeRepository.findById(employee.getEmployeeId());
                    if (employee3.isPresent()) {
                        // 如果数据存在则获取对象
                        Employee employee4 = employee3.get();
                        // 修改之后比较被修改对象的值与前台传递过来的值是否相同，判断该数据是否成功修改
                        if (employee.equals(employee4)) {
                            // 保存操作日志
                            operationLogController.saveOperationLog("UPDATE", employee, user);
                            status = 200;
                            message = "修改成功。";
                        } else {
                            status = 500;
                            message = "服务器出现故障，修改失败，员工信息未被成功修改进数据库。";
                        }
                    } else {
                        status = 500;
                        message = "服务器出现故障，修改失败，员工信息未被成功修改进数据库。";
                    }
                } else {
                    status = 400;
                    message = "修改失败，因为员工信息没有任何改变。";
                }
            } else {
                status = 400;
                message = "修改失败，该员工不存在。";
            }
        }
        response.setCharacterEncoding(ENCODE);
        response.setStatus(status);
        response.getWriter().write(message);
    }

    /**
     * 根据员工 ID 删除员工信息
     *
     * @param user       Principal 获取登录用户信息
     * @param employeeId String 前台传过来的 employeeId
     * @param response   HttpServletResponse 将要返回的状态和信息
     *                   删除之前根据该 employeeId 查询该数据是否存在
     *                   删除之后再次查询该数据是否成功删除
     *                   成功则返回 200 "删除成功。"，否则返回 500 "服务器出现故障，删除失败，员工信息还存在。"
     * @throws IOException 写入响应信息异常
     * @method deleteEmployeeById
     * @author shiminfxcvii
     * @created 2022/4/29 11:20
     */
    @RequestMapping("employee/deleteEmployeeById")
    public void deleteEmployeeById(@NotNull Principal user, String employeeId, HttpServletResponse response) throws IOException {
        if (null != employeeId) {
            if (Pattern.matches("^\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}$", employeeId)) {
                Optional<Employee> employee = employeeRepository.findById(employeeId);
                if (employee.isPresent()) {
                    employeeRepository.deleteById(employeeId);
                    if (employeeRepository.findById(employeeId).isEmpty()) {
                        // 保存操作日志
                        operationLogController.saveOperationLog("DELETE", employee.get(), user);
                        status = 200;
                        message = "删除成功。";
                    } else {
                        status = 500;
                        message = "服务器出现故障，删除失败，员工信息还存在于数据库。";
                    }
                } else {
                    status = 400;
                    message = "删除失败，因为数据库没有该员工信息。";
                }
            } else {
                status = 400;
                message = "删除失败，非法 ID。ID 格式不正确。";
            }
        } else {
            status = 400;
            message = "删除失败，ID 为空。";
        }
        response.setCharacterEncoding(ENCODE);
        response.setStatus(status);
        response.getWriter().write(message);
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
     * @param user      Principal 登录用户
     * @param model     Model 页面模型
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @return "employee" 返回查询后的整个页面
     * @method findEmployeesBy
     * @author shiminfxcvii
     * @created 2022/4/29 11:50
     */
    @RequestMapping("employee/findEmployeesBy")
    public String findEmployeesBy(
            // name ==(等效) value
            @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "direction", defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(name = "property", defaultValue = "createdDate") String property,
            Employee employee,
            @NotNull Principal user,
            @NotNull Model model
    ) throws IllegalAccessException {
        model.addAttribute(
                "employees",
                employeeRepository.findAll(
                        Example.of(
                                employee,
                                // 匹配所有字段的模糊查询并且忽略大小写
                                ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                        ), PageRequest.of(pageNum, pageSize, Sort.by(direction, property, "employeeId"))
                )
        );
        // 保存搜索记录
        searchRecordController.saveSearchRecord(employee, user);
        return "employee";
    }

}