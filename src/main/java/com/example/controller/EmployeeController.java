package com.example.controller;

import com.example.entity.Employee;
import com.example.repository.EmployeeRepository;
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
import java.util.UUID;

@Controller
public class EmployeeController {

    @Resource
    private EmployeeRepository employeeRepository;

    // TODO: jQuery 根据时间自动调节背景颜色！
    // TODO: <input> 如何解决在使用中文输入时的错误？
    // TODO: 学习新一代 thymeleaf-extras-spring security6 的使用方法
    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("logout")
    public String logout() {
        return "logout";
    }

    /*@RequestMapping("loginFailed")
    public String loginFailed() {
        return "loginFailed";
    }*/

    // TODO: 为什么点退出会来的 timeout 页面？
    /*@RequestMapping("timeout")
    public String timeout() {
        return "timeout";
    }*/

    @RequestMapping("employee")
    public String employee(@NotNull Model model, @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        model.addAttribute("employees", employeeRepository.findAll(
                PageRequest.of(pageNum, pageSize, Sort.by("createdDate"))));
        return "employee";
    }

    @RequestMapping("employee/saveEmployee")
    public String saveEmployee(@RequestBody @NotNull Employee employee, @NotNull Model model, @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        employee.setEmployeeId(String.valueOf(UUID.randomUUID()));
        employeeRepository.save(employee);
        model.addAttribute("employees", employeeRepository.findAll(
                PageRequest.of(pageNum, pageSize, Sort.by("createdDate"))));
        return "employee";
    }

    @RequestMapping("employee/deleteEmployee")
    public String deleteEmployee(String employeeId, @NotNull Model model, @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        employeeRepository.deleteById(employeeId);
        model.addAttribute("employees", employeeRepository.findAll(
                PageRequest.of(pageNum, pageSize, Sort.by("createdDate"))));
        return "employee";
    }

    @RequestMapping("employee/updateEmployee")
    public String updateEmployee(@RequestBody Employee employee, @NotNull Model model, @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        employeeRepository.save(employee);
        model.addAttribute("employees", employeeRepository.findAll(
                PageRequest.of(pageNum, pageSize, Sort.by("createdDate"))));
        return "employee";
    }

    @RequestMapping("employee/findEmployee")
    // 如果没有 @RequestBody，就接收不到 jQuery 传过来的值
    public String findEmployee(@RequestBody Employee employee, @NotNull Model model, @RequestParam(value = "pageNum",
            defaultValue = "0") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

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
        ), PageRequest.of(pageNum, pageSize, Sort.by("createdDate"))));
        return "employee";
    }
}