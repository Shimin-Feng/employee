package com.example.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

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
@Table(name = "user", schema = "employee_management")
public class User {
    @Id
    @Column(name = "user_id", unique = true, nullable = false, updatable = false, columnDefinition = "varchar", length = 36)
    private String userId;
    @Column(name = "username", nullable = false, columnDefinition = "varchar", length = 45)
    private String username;
    @Column(name = "password", nullable = false, columnDefinition = "varchar", length = 60)
    private String password;
    @Column(name = "authorities", nullable = false, columnDefinition = "varchar", length = 45)
    private String authorities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return null != userId && Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }
}