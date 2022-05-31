package com.shiminfxcvii.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "employee", schema = "employee_management")
public class Employee {

    @Id
    @Column(name = "employee_id", unique = true, nullable = false, updatable = false, columnDefinition = "varchar", length = 36)
    private String employeeId;
    @Column(name = "employee_name", nullable = false, columnDefinition = "varchar", length = 45)
    private String employeeName;
    @Column(name = "employee_sex", nullable = false, columnDefinition = "varchar", length = 1)
    private String employeeSex;
    @Column(name = "employee_age", nullable = false, columnDefinition = "varchar", length = 2)
    private String employeeAge;
    @Column(name = "employee_id_card", nullable = false, columnDefinition = "varchar", length = 18)
    private String employeeIdCard;
    @Column(name = "employee_address", nullable = false, columnDefinition = "varchar", length = 45)
    private String employeeAddress;
    @Column(name = "employee_phone_number", nullable = false, columnDefinition = "varchar", length = 11)
    private String employeePhoneNumber;
    @Column(name = "created_by", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    private String createdBy;
    @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "varchar", length = 19)
    private String createdDate;
    @Column(name = "last_modified_date", nullable = false, columnDefinition = "varchar", length = 19)
    private String lastModifiedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employeeName, employee.getEmployeeName()) &&
                Objects.equals(employeeIdCard, employee.getEmployeeIdCard()) &&
                Objects.equals(employeeAddress, employee.getEmployeeAddress()) &&
                Objects.equals(employeePhoneNumber, employee.getEmployeePhoneNumber());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}