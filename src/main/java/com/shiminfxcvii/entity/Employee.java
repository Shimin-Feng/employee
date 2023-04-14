package com.shiminfxcvii.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractAuditable;

/**
 * 员工信息实体类
 *
 * @author ShiminFXCVII
 * @since 6/2/2022 5:16 PM
 */
@Entity
@Getter
@Setter
@Table(name = "employee", schema = "employee_management")
public class Employee extends AbstractAuditable<Long, Long> {

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

}