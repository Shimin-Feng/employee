package com.shiminfxcvii.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * 员工信息实体类
 *
 * @author shiminfxcvii
 * @since 6/2/2022 5:16 PM
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "employee", schema = "employee_management")
public class Employee {

    @Id
    @Column(name = "employee_id", unique = true, nullable = false, updatable = false, columnDefinition = "varchar",
            length = 36)
    private String employeeId;
    @Column(name = "employee_name", nullable = false, columnDefinition = "varchar", length = 45)
    private String employeeName;
    @Column(name = "employee_sex", nullable = false, columnDefinition = "varchar", length = 1)
    private Integer employeeSex;
    @Column(name = "employee_age", nullable = false, columnDefinition = "varchar", length = 2)
    private Integer employeeAge;
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
        return null != employeeId && Objects.equals(employeeId, employee.employeeId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
