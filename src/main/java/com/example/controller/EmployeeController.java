package com.example.controller;

import com.example.entity.Employee;
import com.example.entity.SearchRecord;
import com.example.methods.CustomMethods;
import com.example.repository.EmployeeRepository;
import com.example.repository.SearchRecordRepository;
import net.minidev.json.JSONArray;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Controller
public class EmployeeController {

    @Resource
    private EmployeeRepository employeeRepository;
    @Resource
    private SearchRecordRepository searchRecordRepository;

    // TODO: <input> 如何解决在使用中文输入时的错误？
    // TODO: 学习新一代 thymeleaf-extras-spring security6 的使用方法
    // TODO: 统计图
    // TODO: 数据库根据时间自动调整年龄
    // TODO: 尽可能多地合并 ajax
    // TODO: js 中可能有一个判断语句存在错误，记得本来应该判断是否为 -1，结果没比较，因为编译没出错
    // TODO: 在编写 saveOrUpdateEmployee doc 时有件需要做的事情忘记了
    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    /*@RequestMapping("logout")
    public String logout() {
        return "logout";
    }*/

    /*@RequestMapping("loginFailed")
    public String loginFailed() {
        return "loginFailed";
    }*/

    // TODO: 为什么点退出会来的 timeout 页面？
    /*@RequestMapping("timeout")
    public String timeout() {
        return "timeout";
    }*/

    /**
     * 当进入员工管理页面之前会立即执行一次查询所有员工，默认按照添加时间升序排列，每页十条数据
     *
     * @param model     Model 页面模型
     * @param pageNum   Integer 显示该参数值页，默认显示第一页
     * @param pageSize  Integer 页数大小，每页显示多少条数据
     * @param direction Sort.Direction 排序策略，默认升序，ASC 升序，DESC 降序
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
     * @return String "employee" 返回查询后的整个页面
     * @method employee
     * @author $himin F
     * @created 2022/4/29 10:32
     */
    @RequestMapping("employee")
    public String employee(Model model,
                           @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                           @RequestParam(value = "direction", defaultValue = "ASC") Sort.Direction direction,
                           @RequestParam(value = "property", defaultValue = "createdDate") String property) {
        model.addAttribute("employees", employeeRepository.findAll(
                PageRequest.of(pageNum, pageSize, Sort.by(direction, property, "employeeId"))));
        return "employee";
    }

    /**
     * 该方法有两个作用，添加和修改员工信息
     *
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
     * @method saveOrUpdateEmployee
     * @author $himin F
     * @created 2022/4/29 10:59
     */
    @RequestMapping("employee/saveOrUpdateEmployee")
    public void saveOrUpdateEmployee(@RequestBody @NotNull Employee employee, HttpServletResponse response) {
        if (employee.getEmployeeId() == null || Objects.equals(employee.getEmployeeId(), "")) {
            // save
            employee.setEmployeeId(UUID.randomUUID().toString());
            employeeRepository.saveAndFlush(employee);
            // 检查是否成功保存到数据库
            Optional<Employee> employee1 = employeeRepository.findById(employee.getEmployeeId());
            try {
                if (employee1.isPresent()) {
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(200);
                    response.getWriter().write("添加成功。");
                } else {
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(500);
                    response.getWriter().write("添加失败，员工信息未保存。");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // update
            Optional<Employee> employee1 = employeeRepository.findById(employee.getEmployeeId());
            // 判断是否存在该员工
            if (employee1.isPresent()) {
                // 获取对象
                Employee employee2 = employee1.get();
                // 修改之前比较被修改对象的值与前台传递过来的值是否相同，不相同则执行修改操作
                boolean isSame = CustomMethods.isSame(employee, employee2);
                if (!isSame) {
                    // 执行修改操作
                    employeeRepository.saveAndFlush(employee);
                    // 执行修改之后再次查询该员工属性
                    Optional<Employee> employee3 = employeeRepository.findById(employee.getEmployeeId());
                    if (employee3.isPresent()) {
                        // 如果数据存在则获取对象
                        Employee employee4 = employee1.get();
                        // 修改之后比较被修改对象的值与前台传递过来的值是否相同，判断该数据是否成功修改
                        boolean isSame1 = CustomMethods.isSame(employee, employee4);
                        if (isSame1) {
                            try {
                                response.setCharacterEncoding("UTF-8");
                                response.setStatus(200);
                                response.getWriter().write("修改成功。");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
                                response.setCharacterEncoding("UTF-8");
                                response.setStatus(500);
                                response.getWriter().write("服务器出现故障，修改失败，员工信息未被成功修改到数据库。");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } else {
                    try {
                        response.setCharacterEncoding("UTF-8");
                        response.setStatus(400);
                        response.getWriter().write("修改失败，因为员工信息没有任何改变！");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * 根据员工 ID 删除员工信息
     *
     * @param employeeId String 前台传过来的 employeeId
     *                   执行shan
     * @param response   HttpServletResponse 将要返回的状态和信息
     *                   删除之前根据该 employeeId 查询该数据是否存在
     *                   删除之后再次查询该数据是否成功删除
     *                   成功则返回 200 "删除成功。"，否则返回 500 "服务器出现故障，删除失败，员工信息还存在。"
     * @method deleteEmployeeById
     * @author $himin F
     * @created 2022/4/29 11:20
     */
    @RequestMapping("employee/deleteEmployeeById")
    public void deleteEmployeeById(String employeeId, HttpServletResponse response) {
        if (Pattern.matches("^\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}$", employeeId)) {
            Optional<Employee> employee1 = employeeRepository.findById(employeeId);
            if (employee1.isPresent()) {
                employeeRepository.deleteById(employeeId);
                Optional<Employee> employee2 = employeeRepository.findById(employeeId);
                try {
                    if (employee2.isEmpty()) {
                        response.setCharacterEncoding("UTF-8");
                        response.setStatus(200);
                        response.getWriter().write("删除成功。");
                    } else {
                        response.setCharacterEncoding("UTF-8");
                        response.setStatus(500);
                        response.getWriter().write("服务器出现故障，删除失败，员工信息还存在。");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(400);
                    response.getWriter().write("删除失败，该员工信息不存在于数据库！");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            try {
                response.setCharacterEncoding("UTF-8");
                response.setStatus(400);
                response.getWriter().write("删除失败，非法 ID！");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 根据条件和关键字查找搜索记录，用于前台搜索框的 autocomplete，自动完成提示
     *
     * @param username      String 登录用户的用户名
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
     * @param response      HttpServletResponse 将要返回的状态和信息
     * @method findRecordNamesBy
     * @author $himin F
     * @created 2022/4/29 11:34
     */
    @RequestMapping("findRecordNamesBy")
    public void findRecordNamesBy(String username, String searchGroupBy, String recordName, HttpServletResponse response) {
        // this
        List<String> thisRecordNamesOne = searchRecordRepository.findThisRecordNamesOne(username, searchGroupBy, recordName);
        List<String> thisRecordNamesTwo = searchRecordRepository.findThisRecordNamesTwo(username, searchGroupBy, recordName);
        List<String> thisRecordNamesThree = CustomMethods.mergeLists(thisRecordNamesOne, thisRecordNamesTwo);
        // 只取前面十条数据
        List<String> thisRecordNamesFour = CustomMethods.getTenSearchRecords(thisRecordNamesThree);
        // all
        List<String> allRecordNamesFour = new ArrayList<>();
        if (recordName != null && !Objects.equals(recordName, "")) {
            List<String> allRecordNamesOne = searchRecordRepository.findAllRecordNamesOne(searchGroupBy, recordName);
            List<String> allRecordNamesTwo = searchRecordRepository.findAllRecordNamesTwo(searchGroupBy, recordName);
            List<String> allRecordNamesThree = CustomMethods.mergeLists(allRecordNamesOne, allRecordNamesTwo);
            // 只取前面十条数据，如果有的话
            allRecordNamesFour = CustomMethods.getTenSearchRecords(allRecordNamesThree);
        }
        List<String> thisAndAllRecordNamesOne = CustomMethods.mergeLists(thisRecordNamesFour, allRecordNamesFour);
        // 只取前面十条数据
        List<String> thisAndAllRecordNamesTwo = CustomMethods.getTenSearchRecords(thisAndAllRecordNamesOne);
        // 最后判断是否有值，没有值就什么也不用返回
        if (thisAndAllRecordNamesTwo.size() > 0) {
            String thisAndAllRecordNamesThree = JSONArray.toJSONString(thisAndAllRecordNamesTwo);
            try {
                // 不设置编码前台就无法解析汉字
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(thisAndAllRecordNamesThree);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 根据条件和关键字搜索员工信息
     *
     * @param employee  Employee 根据员工的某一个字段和值进行搜索
     * @param model     Model 页面模型
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
     * @param username  登录用户的用户名
     * @return String "employee" 返回查询后的整个页面
     * @method findEmployeesBy
     * @author $himin F
     * @created 2022/4/29 11:50
     */
    @RequestMapping("employee/findEmployeesBy")
    // 如果没有 @RequestBody，就接收不到 jQuery 传过来的值
    public String findEmployeesBy(@RequestBody Employee employee,
                                  Model model,
                                  @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                  @RequestParam(value = "direction", defaultValue = "ASC") Sort.Direction direction,
                                  @RequestParam(value = "property", defaultValue = "createdDate") String property,
                                  @RequestParam(value = "username", defaultValue = "") String username) {
        model.addAttribute("employees", employeeRepository.findAll(Example.of(employee,
                /*
                  .matchingAll()                                返回一个匹配所有字段的 ExampleMatcher 对象
                  .withMatcher(                                 规则匹配器
                       "employeeName",                          propertyPath "employeeName" 需要匹配的字段名
                       ExampleMatcher
                           .GenericPropertyMatchers
                           .contains()                          匹配规则，表示 like %？%，主要用于模糊查询，匹配任意位置
                  )
                  .withIgnoreCase("employeeName")               忽略数据库该字段的大小写，也可以多个字段参数
                  .withIgnoreCase(true)                         默认忽略大小写，所以不需要设置
                  .withIgnorePaths("employeeId")                需要忽略匹配的数据库字段。不对 "employeeId" 字段进行任何处理
                 */
                ExampleMatcher
                        .matchingAll()
                        // TODO: 每个字段都需要单独设置规则匹配器。如何简化？
                        .withMatcher("employeeName", ExampleMatcher.GenericPropertyMatchers.contains())
                        .withMatcher("employeeSex", ExampleMatcher.GenericPropertyMatchers.contains())
                        .withMatcher("employeeAge", ExampleMatcher.GenericPropertyMatchers.contains())
                        .withMatcher("employeeIdCard", ExampleMatcher.GenericPropertyMatchers.contains())
                        .withMatcher("employeeAddress", ExampleMatcher.GenericPropertyMatchers.contains())
                        .withMatcher("employeePhoneNumber", ExampleMatcher.GenericPropertyMatchers.contains())
                        .withMatcher("createdBy", ExampleMatcher.GenericPropertyMatchers.contains())
                        .withMatcher("createdDate", ExampleMatcher.GenericPropertyMatchers.contains())
                        .withMatcher("lastModifiedDate", ExampleMatcher.GenericPropertyMatchers.contains())
                        .withIgnorePaths("employeeId")
        ), PageRequest.of(pageNum, pageSize, Sort.by(direction, property, "employeeId"))));
        if (!Objects.equals(username, "")) {
            String searchGroupBy = "";
            String recordName = "";
            if (employee.getEmployeeName() != null && !Objects.equals(employee.getEmployeeName(), "")) {
                searchGroupBy = "employeeName";
                recordName = employee.getEmployeeName();
            } else if (employee.getEmployeeSex() != null && !Objects.equals(employee.getEmployeeSex(), "")) {
                searchGroupBy = "employeeSex";
                recordName = employee.getEmployeeSex();
            } else if (employee.getEmployeeAge() != null && !Objects.equals(employee.getEmployeeAge(), "")) {
                searchGroupBy = "employeeAge";
                recordName = employee.getEmployeeAge();
            } else if (employee.getEmployeeIdCard() != null && !Objects.equals(employee.getEmployeeIdCard(), "")) {
                searchGroupBy = "employeeIdCard";
                recordName = employee.getEmployeeIdCard();
            } else if (employee.getEmployeeAddress() != null && !Objects.equals(employee.getEmployeeAddress(), "")) {
                searchGroupBy = "employeeAddress";
                recordName = employee.getEmployeeAddress();
            } else if (employee.getEmployeePhoneNumber() != null && !Objects.equals(employee.getEmployeePhoneNumber(), "")) {
                searchGroupBy = "employeePhoneNumber";
                recordName = employee.getEmployeePhoneNumber();
            } else if (employee.getCreatedBy() != null && !Objects.equals(employee.getCreatedBy(), "")) {
                searchGroupBy = "createdBy";
                recordName = employee.getCreatedBy();
            } else if (employee.getCreatedDate() != null && !Objects.equals(employee.getCreatedDate(), "")) {
                searchGroupBy = "createdDate";
                recordName = employee.getCreatedDate();
            } else if (employee.getLastModifiedDate() != null && !Objects.equals(employee.getLastModifiedDate(), "")) {
                searchGroupBy = "lastModifiedDate";
                recordName = employee.getLastModifiedDate();
            }
            // 最后验证该搜索是否有值 (有一次数据库存入了两条空值记录!)
            if (!Objects.equals(recordName, "")) {
                searchRecordRepository.save(new SearchRecord(UUID.randomUUID().toString(), searchGroupBy, recordName, username, new Date()));
            }
        }
        return "employee";
    }
}