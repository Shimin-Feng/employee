package com.shiminfxcvii.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 记录 user 每一次的搜索内容，并与 username 关联，username 唯一
 * 无法改用为 record 记录类
 * 因为
 * 实体类必须是非 final
 * 实体类需要有一个无参数的构造函数，要么是 public 要么 protected
 * 实体属性必须是非 final
 *
 * @author shiminfxcvii
 * @since 2022/4/24 0:11 周日
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
        if (null == o || getClass() != o.getClass()) return false;
        SearchRecord searchRecord = (SearchRecord) o;
        return null != recordId && Objects.equals(recordId, searchRecord.recordId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}