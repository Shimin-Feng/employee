package com.shiminfxcvii.repository;

import com.shiminfxcvii.entity.SearchRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @interface SearchRecordRepository
 * @created 2022/4/24 0:23 周日
 * @description 查询和删除搜索记录，用于前台搜索框 autocomplete
 */
@Repository
public interface SearchRecordRepository extends JpaRepository<SearchRecord, String> {

    /**
     * 查找符合条件的搜索名 %?
     *
     * @param username      登陆用户名
     * @param searchGroupBy 搜索字段
     * @param recordName    搜索名称
     * @return 返回符合条件的搜索名 record_name
     * @method findThisRecordNamesOne
     * @author shiminfxcvii
     * @created 2022/5/20 14:53
     * TODO: 如何将这两个方法合并？
     */
    @Query(value = "SELECT record_name FROM (SELECT record_name, MAX(created_date) mcd FROM employee_management.search_record WHERE username = ?1 AND search_group_by = ?2 AND IF('' != ?3, record_name LIKE CONCAT(?3, '%'), TRUE) GROUP BY record_name) AS rncd ORDER BY mcd DESC LIMIT 0, 10;", nativeQuery = true)
    LinkedHashSet<String> findThisRecordNamesOne(@NotNull String username, @NotNull String searchGroupBy, @Nullable String recordName);

    /**
     * 查找符合条件的搜索名 %?%
     *
     * @param username      登陆用户名
     * @param searchGroupBy 搜索字段
     * @param recordName    搜索名称
     * @return 返回符合条件的搜索名 record_name
     * @method findThisRecordNamesTwo
     * @author shiminfxcvii
     * @created 2022/5/20 14:55
     */
    @Query(value = "SELECT record_name FROM (SELECT record_name, MAX(created_date) mcd FROM employee_management.search_record WHERE username = ?1 AND search_group_by = ?2 AND record_name LIKE CONCAT('%', ?3, '%') GROUP BY record_name) AS rncd ORDER BY mcd DESC LIMIT 0, 10;", nativeQuery = true)
    LinkedHashSet<String> findThisRecordNamesTwo(@NotNull String username, @NotNull String searchGroupBy, @NotNull String recordName);

    /*@Query(value = "SELECT record_name FROM (SELECT record_name, MAX(created_date) mcd, COUNT(record_name) c FROM employee_management.search_record WHERE search_group_by = ?1 AND record_name LIKE CONCAT(?2, '%') GROUP BY record_name) AS rncdc ORDER BY c DESC, mcd DESC LIMIT 0, 10;", nativeQuery = true)
    List<String> findAllRecordNamesOne(@Param("searchGroupBy") String searchGroupBy, @Param("recordName") String recordName);

    @Query(value = "SELECT record_name FROM (SELECT record_name, MAX(created_date) mcd, COUNT(record_name) c FROM employee_management.search_record WHERE search_group_by = ?1 AND record_name LIKE CONCAT('%', ?2, '%') GROUP BY record_name) AS rncdc ORDER BY c DESC, mcd DESC LIMIT 0, 10;", nativeQuery = true)
    List<String> findAllRecordNamesTwo(@Param("searchGroupBy") String searchGroupBy, @Param("recordName") String recordName);*/
}