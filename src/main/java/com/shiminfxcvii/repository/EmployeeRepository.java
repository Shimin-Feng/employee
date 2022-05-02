package com.shiminfxcvii.repository;

import com.shiminfxcvii.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
}