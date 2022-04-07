package com.example.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Serializable
 * 系列化的用途：
 * 需要把内存中的对象状态保存到一个文件中或者数据库中时候
 * 需要把对象通过网络进行传播的时候
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Employee implements Serializable {

    @Id
    private String employeeId;
    @Column
    private String employeeName;
    @Column
    private String employeeSex;
    @Column
    private Integer employeeAge;
    @Column
    private String employeeIdCard;
    @Column
    private String employeeAddress;
    @Column
    private String employeePhoneNumber;
    @CreatedBy
    @Column
    private String createdBy;
    @CreatedDate
    @Column
    private Date createdDate;
    @LastModifiedDate
    @Column
    private Date lastModifiedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Employee employee = (Employee) o;
        return employeeId != null && Objects.equals(employeeId, employee.employeeId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}