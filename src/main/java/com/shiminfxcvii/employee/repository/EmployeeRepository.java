package com.shiminfxcvii.employee.repository;

import com.shiminfxcvii.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 员工信息操作工厂接口
 *
 * @author ShiminFXCVII
 * @since 2022/6/2 20:02
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}