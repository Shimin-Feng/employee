package com.shiminfxcvii.repository;

import com.shiminfxcvii.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 员工信息操作接口
 *
 * @author shiminfxcvii
 * @since 2022/6/2 20:02
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
}