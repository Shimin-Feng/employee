package com.shiminfxcvii.employee.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户信息表
 *
 * @author ShiminFXCVII
 * @since 2022/6/2 20:01
 */
@Accessors(chain = true)
@Entity
@Getter
@Setter
@Table(name = "user", schema = "employee_management")
public class User extends AbstractAuditable<User, Long> {

    @Column(name = "username", nullable = false, columnDefinition = "varchar", length = 64)
    private String username;
    @Column(name = "password", nullable = false, columnDefinition = "varchar", length = 128)
    private String password;
    @Column(name = "authorities", nullable = false, columnDefinition = "varchar", length = 64)
    private String authorities;

    /**
     * 用户详情
     *
     * @since 2023/6/7 21:32
     */
    private transient UserDetails userDetails;

}