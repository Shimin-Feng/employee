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
import java.util.Date;
import java.util.Objects;

/**
 * @Name SearchRecord
 * @Author $himin F
 * @Date 2022/4/24 0:11 周日
 * @Version 1.0
 * @description: 记录 user 每一次的搜索内容，并与 username 关联，username 唯一
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
@Table(name = "search_record", schema = "test_database")
public class SearchRecord {
    @Id
    @Column(name = "record_id", unique = true, nullable = false, updatable = false, columnDefinition = "varchar", length = 36)
    String recordId;
    @Column(name = "search_group_by", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    String searchGroupBy;
    @Column(name = "record_name", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    String recordName;
    @Column(name = "username", nullable = false, updatable = false, columnDefinition = "varchar", length = 45)
    String username;
    @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "datetime")
    Date createdDate;

    public SearchRecord(String recordId, String searchGroupBy, String recordName, String username, Date createdDate) {
        this.recordId = recordId;
        this.searchGroupBy = searchGroupBy;
        this.recordName = recordName;
        this.username = username;
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SearchRecord that = (SearchRecord) o;
        return recordId != null && Objects.equals(recordId, that.recordId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}