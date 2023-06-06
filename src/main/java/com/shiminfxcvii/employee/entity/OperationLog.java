package com.shiminfxcvii.employee.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.AbstractAuditable;

/**
 * 员工管理操作日志实体类
 *
 * @author ShiminFXCVII
 * @since 2022/5/1 22:36 周日
 */
@Accessors(chain = true)
@Entity
@Getter
@Setter
@Table(name = "operation_logs", schema = "employee_management")
public class OperationLog extends AbstractAuditable<User, Long> {

    @Column(name = "dml", nullable = false, updatable = false, columnDefinition = "varchar", length = 6)
    private String dml;
    @Column(name = "employee_id", nullable = false, updatable = false, columnDefinition = "varchar", length = 36)
    private Long employeeId;
    @Column(name = "employee_name", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    private String employeeName;
    @Column(name = "employee_sex", nullable = false, updatable = false, columnDefinition = "varchar", length = 1)
    private Integer employeeSex;
    @Column(name = "employee_age", nullable = false, updatable = false, columnDefinition = "varchar", length = 2)
    private Integer employeeAge;
    @Column(name = "employee_id_card", nullable = false, updatable = false, columnDefinition = "varchar", length = 18)
    private String employeeIdCard;
    @Column(name = "employee_address", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    private String employeeAddress;
    @Column(name = "employee_phone_number", nullable = false, updatable = false, columnDefinition = "varchar", length = 11)
    private String employeePhoneNumber;

}