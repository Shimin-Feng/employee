package com.shiminfxcvii.employee.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.AbstractAuditable;

/**
 * 记录 user 每一次的搜索内容，并与 username 关联，username 唯一
 * 无法改用为 record 记录类
 * 因为
 * 实体类必须是非 final
 * 实体类需要有一个无参数的构造函数，要么是 public 要么 protected
 * 实体属性必须是非 final
 *
 * @author ShiminFXCVII
 * @since 2022/4/24 0:11 周日
 */
@Accessors(chain = true)
@Entity
@Getter
@Setter
@Table(name = "search_record", schema = "employee_management")
public class SearchRecord extends AbstractAuditable<User, Long> {

    @Column(name = "search_group_by", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    private String searchGroupBy;
    @Column(name = "record_name", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    private String recordName;

}