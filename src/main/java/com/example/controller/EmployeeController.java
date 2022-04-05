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

    @RequestMapping("/timeout")
    public String timeout() {
        return "timeout";
    }

    @RequestMapping("employee/saveEmployee")
    public String saveEmployee(@RequestBody @NotNull Employee employee) {
        employee.setEmployeeId(String.valueOf(UUID.randomUUID()));
        employeeRepository.save(employee);
        return "index";
    }

    @RequestMapping("employee/findEmployee")
    // TODO: 搜索后的数据没有显示出来
    public String findById(String param, @NotNull Model model, @RequestParam(value = "pageNum",
            defaultValue = "0") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("createdDate"));
        Employee employee = new Employee();
        employee.setEmployeeName(param);


        // endsWith() 是 employeeName 结尾为喜欢的数据
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withMatcher("employeeName",
                ExampleMatcher.GenericPropertyMatchers.contains()).withIgnorePaths("employeeId");


        //创建实例
        Example<Employee> example = Example.of(employee, exampleMatcher);
        Page<Employee> page = employeeRepository.findAll(example, pageable);
        model.addAttribute("employee", page);
        return "redirect:/employee/index";
    }

}