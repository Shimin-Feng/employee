package com.shiminfxcvii.controller;

import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.model.cmd.EmployeeCmd;
import com.shiminfxcvii.model.query.EmployeeQuery;
import com.shiminfxcvii.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.shiminfxcvii.util.Constants.*;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.MediaType.ALL_VALUE;

/**
 * 操作员工信息 ———— CRUD
 *
 * @author shiminfxcvii
 * @since 2022/5/1 14:50
 */
@Controller
@RequestMapping("employee")
@Tag(name = "employeeController", description = "操作员工信息 ———— CRUD")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Lazy
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @Operation(description = "员工管理界面", method = "GET", summary = "测试", tags = "employeeController")
    public String employee(Model model) {
        employeeService.employee(model);
        return "employee";
    }

    @RequestMapping(
            value = "saveOrUpdateEmployee",
            method = {RequestMethod.POST, RequestMethod.PATCH},
            params = {EMPLOYEE_NAME, EMPLOYEE_ID_CARD, EMPLOYEE_ADDRESS, EMPLOYEE_PHONE_NUMBER, EMPLOYEE_ID},
            headers = {CACHE_CONTROL, X_CSRF_TOKEN},
            consumes = ALL_VALUE,
            produces = ALL_VALUE
    )
    public ResponseEntity<String> saveOrUpdateEmployee(@NotNull(value = "用户信息不能为空") Principal user,
                                                       @RequestBody @Validated EmployeeCmd cmd,
                                                       RequestEntity<Employee> request)
            throws IllegalAccessException {
        return employeeService.saveOrUpdateEmployee(user, cmd, request);
    }

    @DeleteMapping(
            value = "deleteEmployeeById",
            params = ID,
            headers = {CACHE_CONTROL, X_CSRF_TOKEN},
            consumes = MediaType.IMAGE_JPEG_VALUE,
            produces = ALL_VALUE
    )
    public ResponseEntity<String> deleteEmployeeById(@NotNull(value = "无法获取用户信息") Principal user,
                                                     @NotNull(value = "员工 id 不能为空") String id)
            throws IllegalAccessException {
        return employeeService.deleteEmployeeById(user, id);
    }

    @GetMapping(
            value = "findEmployeesBy",
            params = {PAGE_NUM, PAGE_SIZE, DIRECTION, PROPERTY},
            headers = {CACHE_CONTROL, X_CSRF_TOKEN},
            consumes = ALL_VALUE,
            produces = MediaType.TEXT_HTML_VALUE
    )
    public String findEmployeesBy(@RequestParam(value = PAGE_NUM, defaultValue = ZERO) Integer pageNum,
                                  @RequestParam(value = PAGE_SIZE, defaultValue = TEN) Integer pageSize,
                                  @RequestParam(value = DIRECTION, defaultValue = ASC) Sort.Direction direction,
                                  @RequestParam(value = PROPERTY, defaultValue = CREATED_DATE) String property,
                                  EmployeeQuery query,
                                  Model model) {
        employeeService.findEmployeesBy(pageNum, pageSize, direction, property, query, model);
        return "employee";
    }

}
