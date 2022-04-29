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
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Employee {

    @Id
    private String employeeId;
    @Column
    private String employeeName;
    @Column
    private String employeeSex;
    @Column
    private String employeeAge;
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
    private String createdDate;
    @LastModifiedDate
    @Column
    private String lastModifiedDate;

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