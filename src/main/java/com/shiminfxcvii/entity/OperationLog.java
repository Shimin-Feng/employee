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

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description 员工管理操作日志实体类
 * @class OperationLog
 * @created 2022/5/1 22:36 周日
 * @see Employee
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "operation_logs", schema = "employee_management")
public class OperationLog {
    @Id
    @Column(name = "log_id", unique = true, nullable = false, updatable = false, columnDefinition = "varchar", length = 36)
    private String logId;
    @Column(name = "dml", nullable = false, updatable = false, columnDefinition = "varchar", length = 6)
    private String dml;
    @Column(name = "employee_id", nullable = false, updatable = false, columnDefinition = "varchar", length = 36)
    private String employeeId;
    @Column(name = "employee_name", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    private String employeeName;
    @Column(name = "employee_sex", nullable = false, updatable = false, columnDefinition = "varchar", length = 1)
    private String employeeSex;
    @Column(name = "employee_age", nullable = false, updatable = false, columnDefinition = "varchar", length = 2)
    private String employeeAge;
    @Column(name = "employee_id_card", nullable = false, updatable = false, columnDefinition = "varchar", length = 18)
    private String employeeIdCard;
    @Column(name = "employee_address", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    private String employeeAddress;
    @Column(name = "employee_phone_number", nullable = false, updatable = false, columnDefinition = "varchar", length = 11)
    private String employeePhoneNumber;
    @Column(name = "created_by", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    private String createdBy;
    @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "varchar", length = 19)
    private String createdDate;
    @Column(name = "last_modified_date", nullable = false, updatable = false, columnDefinition = "varchar", length = 19)
    private String lastModifiedDate;
    @Column(name = "username", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    private String username;
    @Column(name = "date_time", nullable = false, updatable = false, columnDefinition = "varchar", length = 19)
    private String dateTime;

    // Java Util Objects 7+ Version
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        OperationLog log = (OperationLog) o;
        return logId.equals(log.logId) && dml.equals(log.dml) && employeeId.equals(log.employeeId) && employeeName.equals(log.employeeName) && employeeSex.equals(log.employeeSex) && employeeAge.equals(log.employeeAge) && employeeIdCard.equals(log.employeeIdCard) && employeeAddress.equals(log.employeeAddress) && employeePhoneNumber.equals(log.employeePhoneNumber) && createdBy.equals(log.createdBy) && createdDate.equals(log.createdDate) && lastModifiedDate.equals(log.lastModifiedDate) && username.equals(log.username) && dateTime.equals(log.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logId, dml, employeeId, employeeName, employeeSex, employeeAge, employeeIdCard, employeeAddress, employeePhoneNumber, createdBy, createdDate, lastModifiedDate, username, dateTime);
    }
}