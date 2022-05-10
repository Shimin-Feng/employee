package com.shiminfxcvii.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @class SearchRecord
 * @created 2022/4/24 0:11 周日
 * @description 记录 user 每一次的搜索内容，并与 username 关联，username 唯一
 * 无法改用为 record 记录类
 * 因为
 * 实体类必须是非 final
 * 实体类需要有一个无参数的构造函数，要么是 public 要么 protected
 * 实体属性必须是非 final
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "search_record", schema = "employee_management")
public class SearchRecord {
    @Id
    @Column(name = "record_id", unique = true, nullable = false, updatable = false, columnDefinition = "varchar", length = 36)
    private String recordId;
    @Column(name = "search_group_by", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    private String searchGroupBy;
    @Column(name = "record_name", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    private String recordName;
    @Column(name = "username", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    private String username;
    @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "datetime")
    private LocalDateTime createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SearchRecord that = (SearchRecord) o;
        return null != recordId && Objects.equals(recordId, that.recordId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}