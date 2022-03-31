package com.example.controller;

import com.example.entity.Employee;
import com.example.repository.EmployeeRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping(method = RequestMethod.GET)
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    // login.html 界面 <form> 表单设置为 th:action="@{/employee/index}" 提交后为什么还是来到了这个登录界面？
    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("employee/index")
    public String index(@NotNull Model model) {
        model.addAttribute("employee", employeeRepository.findAll());
        return "index";
    }

    @DeleteMapping("employee/deleteEmployee/{employeeId}")
    public String deleteEmployee(@PathVariable("employeeId") String employeeId) {
        employeeRepository.deleteById(employeeId);
        return "index";
    }

    @PostMapping("employee/updateEmployee")
    // @ModelAttribute null
    // @JsonFormat null
    // @RequestParam 400
    // @RequestAttribute 400
    // @RequestBody 415
    public String updateEmployee(@RequestBody Employee employee) {
        employeeRepository.save(employee);
        return "index";
    }

    @PostMapping("employee/save")
    public String save() {
        return "save";
    }

    @PostMapping("employee/saveEmployee")
    public String saveEmployee(@ModelAttribute Employee employee, @NotNull Model model) {
        employee.setEmployeeId(String.valueOf(UUID.randomUUID()));
        employeeRepository.save(employee);
        model.addAttribute("employee", employeeRepository.findAll());
        return "redirect:employee/index";
    }

    @GetMapping("employee/findById/{employeeId}")
    public String findById(@PathVariable("employeeId") String employeeId, @NotNull Model model) {
        model.addAttribute("employee", employeeRepository.getById(employeeId));
        return "redirect:employee/index";
    }
}