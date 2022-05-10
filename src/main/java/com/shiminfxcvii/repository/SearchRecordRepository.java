package com.shiminfxcvii.repository;

import com.shiminfxcvii.entity.SearchRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @class SearchRecordRepository
 * @created 2022/4/24 0:23 周日
 * @description 查询搜索记录，用于前台搜索框 autocomplete
 */
@Repository
public interface SearchRecordRepository extends JpaRepository<SearchRecord, String> {
    // 删除需要显示声明事物 @Transactional
    @Transactional
    void deleteByRecordName(String recordName);

    // 参数在语句最后时不可以有分号
    @Query(value = "SELECT COUNT(record_name) FROM employee_management.search_record WHERE username = ?1 AND search_group_by = ?2 AND record_name = ?3", nativeQuery = true)
    Byte findThisRecordNamesBy(@Param("username") String username, @Param("searchGroupBy") String searchGroupBy, @Param("recordName") String recordName);

    @Query(value = "SELECT record_name FROM (SELECT record_name, MAX(created_date) cd FROM employee_management.search_record WHERE username = ?1 AND search_group_by = ?2 AND IF(?3 != '', record_name LIKE CONCAT(?3, '%'), TRUE) GROUP BY record_name) AS rncd ORDER BY cd DESC LIMIT 0, 10;", nativeQuery = true)
    List<String> findThisRecordNamesOne(@Param("username") String username, @Param("searchGroupBy") String searchGroupBy, @Param("recordName") String recordName);

    @Query(value = "SELECT record_name FROM (SELECT record_name, MAX(created_date) cd FROM employee_management.search_record WHERE username = ?1 AND search_group_by = ?2 AND IF(?3 != '', record_name LIKE CONCAT('%', ?3, '%'), TRUE) GROUP BY record_name) AS rncd ORDER BY cd DESC LIMIT 0, 10;", nativeQuery = true)
    List<String> findThisRecordNamesTwo(@Param("username") String username, @Param("searchGroupBy") String searchGroupBy, @Param("recordName") String recordName);

    @Query(value = "SELECT record_name FROM (SELECT record_name, MAX(created_date) cd, COUNT(record_name) c FROM employee_management.search_record WHERE search_group_by = ?1 AND record_name LIKE CONCAT(?2, '%') GROUP BY record_name) AS rncdc ORDER BY c DESC, cd DESC LIMIT 0, 10;", nativeQuery = true)
    List<String> findAllRecordNamesOne(@Param("searchGroupBy") String searchGroupBy, @Param("recordName") String recordName);

    @Query(value = "SELECT record_name FROM (SELECT record_name, MAX(created_date) cd, COUNT(record_name) c FROM employee_management.search_record WHERE search_group_by = ?1 AND record_name LIKE CONCAT('%', ?2, '%') GROUP BY record_name) AS rncdc ORDER BY c DESC, cd DESC LIMIT 0, 10;", nativeQuery = true)
    List<String> findAllRecordNamesTwo(@Param("searchGroupBy") String searchGroupBy, @Param("recordName") String recordName);
}