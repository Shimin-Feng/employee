package com.example.controller;

import com.example.entity.Employee;
import com.example.repository.EmployeeRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.UUID;

@Controller
public class EmployeeController {

    @Resource
    private EmployeeRepository employeeRepository;

    // TODO: jQuery 根据时间自动调节背景颜色！
    // TODO: <input> 如何解决在使用中文输入时的错误？
    // TODO: 如何解决 HTML 中那些不该报错的报错？
    // TODO: login.html 界面 <form> 表单设置为 th:action="@{/employee/index}" 提交后为什么还是来到了这个登录界面？
    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping("employee/index")
    public String index(@NotNull Model model, @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("createdDate"));
        Page<Employee> employees = employeeRepository.findAll(pageable);
        model.addAttribute("employees", employees);
        return "index";
    }

    @RequestMapping("employee/deleteEmployee")
    public String deleteEmployee(String employeeId) {
        employeeRepository.deleteById(employeeId);
        // TODO: 什么时候能做到删除一个 employee 后无刷新顶替一条数据时就不重定向
        // return "index";
        // TODO: 为什么重定向设置不起作用？是因为发送的是 ajax 请求吗？
        // TODO: 删除数据之后没有数据顶替
        return "redirect:/employee/index";
    }

    @RequestMapping("employee/updateEmployee")
    public String updateEmployee(@RequestBody Employee employee) {
        employeeRepository.save(employee);
        return "index";
    }

    // TODO: 为什么点退出会来的 timeout 页面？
    @RequestMapping("/timeout")
    public String timeout() {
        return "timeout";
    }

    @RequestMapping("employee/saveEmployee")
    public String saveEmployee(@RequestBody @NotNull Employee employee) {
        employee.setEmployeeId(String.valueOf(UUID.randomUUID()));
        employeeRepository.save(employee);
        return "redirect:/employee/index";
    }

    @RequestMapping("employee/findEmployee")
    // TODO: 搜索后的数据在页面没有显示出来
    // 如果没有 @RequestBody，就接收不到 jQuery 传过来的值
    // http://localhost:8080/employee/index?pageNum=1
    // http://localhost:8080/employee/findEmployee?pageNum=1
    public String findEmployee(@RequestBody Employee employee, @NotNull Model model, @RequestParam(value = "pageNum",
            defaultValue = "0") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("createdDate"));
        // TODO: 如何匹配多个字段？比如 String[] params，我要如何匹配数组的值给对象的属性


        // .matchingAll()                                返回一个匹配所有字段的 ExampleMatcher 对象
        // .withMatcher(                                 规则匹配器
        //      "employeeName",                          propertyPath "employeeName" 需要匹配的字段名
        //      ExampleMatcher
        //          .GenericPropertyMatchers
        //          .contains()                          匹配规则，表示 like %？%，主要用于模糊查询，匹配任意位置
        // )
        // .withIgnoreCase("employeeName")               忽略数据库该字段的大小写，也可以多个字段参数
        // .withIgnoreCase(true)                         默认忽略大小写，所以不需要设置
        // .withIgnorePaths("employeeId")                需要忽略匹配的数据库字段。不对 "employeeId" 字段进行任何处理
        ExampleMatcher exampleMatcher = ExampleMatcher
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
                .withIgnorePaths("employeeId");


        Example<Employee> example = Example.of(employee, exampleMatcher);
        System.out.println("---------------------------------------" + exampleMatcher);
        Page<Employee> employees = employeeRepository.findAll(example, pageable);
        model.addAttribute("employees", employees);
        return "index";
    }

}