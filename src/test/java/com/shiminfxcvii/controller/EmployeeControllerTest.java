package com.shiminfxcvii.controller;

import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;

/**
 * @author ShiminFXCVII
 * @since 2022/5/28 3:05 周六
 */
@SpringBootTest
class EmployeeControllerTest {

    private final Employee employee = new Employee();
    private final String path;
    @Autowired
    private EmployeeRepository employeeRepository;

    EmployeeControllerTest(String path) {
        this.path = path;
    }

    @Test
    void findEmployeesBy() {
//        Employee employee = new Employee();
//        employee.setEmployeeId("");
//        employee.setEmployeeName("");
//        employee.setEmployeeSex("");
        employee.setEmployeeAge(32);
//        employee.setEmployeeIdCard("");
        employee.setEmployeeAddress("中国上海");
//        employee.setEmployeePhoneNumber("156");
//        employee.setCreatedBy("");
//        employee.setCreatedDate("");
//        employee.setLastModifiedDate("");
        List<Employee> employeeList = employeeRepository.findAll(
                Example.of(
                        employee,
                        // 匹配所有字段的模糊查询并且忽略大小写
                        ExampleMatcher.matchingAny().withStringMatcher(CONTAINING)
                )
        );
        employeeList.forEach(System.out::println);

        long count = employeeRepository.count(Example.of(employee));
        System.out.println(count);
        System.out.println(path);
    }
}