package com.shiminfxcvii.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @CreatedDate
    @Column(name = "date_time", nullable = false, updatable = false, columnDefinition = "varchar", length = 19)
    private String dateTime;

    // IntelliJ Default Version
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperationLog that = (OperationLog) o;

        if (!logId.equals(that.logId)) return false;
        if (!dml.equals(that.dml)) return false;
        if (!employeeId.equals(that.employeeId)) return false;
        if (!employeeName.equals(that.employeeName)) return false;
        if (!employeeSex.equals(that.employeeSex)) return false;
        if (!employeeAge.equals(that.employeeAge)) return false;
        if (!employeeIdCard.equals(that.employeeIdCard)) return false;
        if (!employeeAddress.equals(that.employeeAddress)) return false;
        if (!employeePhoneNumber.equals(that.employeePhoneNumber)) return false;
        if (!createdBy.equals(that.createdBy)) return false;
        if (!createdDate.equals(that.createdDate)) return false;
        if (!lastModifiedDate.equals(that.lastModifiedDate)) return false;
        if (!username.equals(that.username)) return false;
        return dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        int result = logId.hashCode();
        result = 31 * result + dml.hashCode();
        result = 31 * result + employeeId.hashCode();
        result = 31 * result + employeeName.hashCode();
        result = 31 * result + employeeSex.hashCode();
        result = 31 * result + employeeAge.hashCode();
        result = 31 * result + employeeIdCard.hashCode();
        result = 31 * result + employeeAddress.hashCode();
        result = 31 * result + employeePhoneNumber.hashCode();
        result = 31 * result + createdBy.hashCode();
        result = 31 * result + createdDate.hashCode();
        result = 31 * result + lastModifiedDate.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + dateTime.hashCode();
        return result;
    }
}