package com.shiminfxcvii.employee.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.AbstractAuditable;

/**
 * 员工表
 *
 * @author ShiminFXCVII
 * @since 6/2/2022 5:16 PM
 */
@Accessors(chain = true)
@Entity
@Getter
@Setter
@Table(name = "employee", schema = "employee_management")
public class Employee extends AbstractAuditable<User, Long> {

    /**
     * 员工姓名
     */
    @Column(name = "employee_name", nullable = false, columnDefinition = "varchar", length = 45)
    private String employeeName;
    /**
     * 员工性别
     */
    @Column(name = "employee_sex", nullable = false, columnDefinition = "varchar", length = 1)
    private Integer employeeSex;
    /**
     * 员工年龄
     */
    @Column(name = "employee_age", nullable = false, columnDefinition = "varchar", length = 2)
    private Integer employeeAge;
    /**
     * 员工神风账号码
     */
    @Column(name = "employee_id_card", nullable = false, columnDefinition = "varchar", length = 18)
    private String employeeIdCard;
    /**
     * 员工住址
     */
    @Column(name = "employee_address", nullable = false, columnDefinition = "varchar", length = 45)
    private String employeeAddress;
    /**
     * 员工电话
     */
    @Column(name = "employee_phone_number", nullable = false, columnDefinition = "varchar", length = 11)
    private String employeePhoneNumber;

}