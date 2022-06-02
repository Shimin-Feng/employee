package com.shiminfxcvii.repository;

import com.shiminfxcvii.entity.SearchRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;

/**
 * 查询和删除搜索记录，用于前台搜索框 autocomplete
 *
 * @author shiminfxcvii
 * @since 2022/4/24 0:23 周日
 */
@Repository
public interface SearchRecordRepository extends JpaRepository<SearchRecord, String> {
    /**
     * 查找符合条件的搜索名 %? / %?%
     *
     * @param username      登陆用户名
     * @param searchGroupBy 搜索字段
     * @param recordName    搜索名称
     * @return 返回符合条件的搜索名 record_name
     * @method findThisRecordNamesOne
     * @author shiminfxcvii
     * @since 2022/5/20 14:53
     */
    @Query(
            value = "SELECT record_name FROM (SELECT record_name, MAX(created_date) mcd FROM employee_management.search_record WHERE username = ?1 AND search_group_by = ?2 AND IF('' != ?3, record_name LIKE ?3, TRUE) GROUP BY record_name) AS rncd ORDER BY mcd DESC LIMIT 0, 10;",
            nativeQuery = true
    )
    LinkedHashSet<String> findThisRecordNames(@NotNull String username, @NotNull String searchGroupBy, @Nullable String recordName);
}