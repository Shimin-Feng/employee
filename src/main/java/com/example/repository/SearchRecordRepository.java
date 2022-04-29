package com.example.repository;

import com.example.entity.SearchRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Name SearchRecordRepository
 * @Author $himin F
 * @Date 2022/4/24 0:23 周日
 * @Version 1.0
 * @description: 查询搜索记录，用于前台 autocomplete
 */
public interface SearchRecordRepository extends JpaRepository<SearchRecord, String> {
    @Query(value = "SELECT record_name FROM (SELECT record_name, MAX(created_date) cd FROM search_record WHERE username = ?1 AND search_group_by = ?2 AND IF(?3 != '', record_name LIKE CONCAT(?3, '%'), TRUE) GROUP BY record_name) AS rncd ORDER BY cd DESC LIMIT 0, 10;", nativeQuery = true)
    List<String> findThisRecordNamesOne(@Param("username") String username, @Param("searchGroupBy") String searchGroupBy, @Param("recordName") String recordName);

    @Query(value = "SELECT record_name FROM (SELECT record_name, MAX(created_date) cd FROM search_record WHERE username = ?1 AND search_group_by = ?2 AND IF(?3 != '', record_name LIKE CONCAT('%', ?3, '%'), TRUE) GROUP BY record_name) AS rncd ORDER BY cd DESC LIMIT 0, 10;", nativeQuery = true)
    List<String> findThisRecordNamesTwo(@Param("username") String username, @Param("searchGroupBy") String searchGroupBy, @Param("recordName") String recordName);

    @Query(value = "SELECT record_name FROM (SELECT record_name, MAX(created_date) cd, COUNT(record_name) c FROM test_database.search_record WHERE search_group_by = ?1 AND record_name LIKE CONCAT(?2, '%') GROUP BY record_name) AS rncdc ORDER BY c DESC, cd DESC LIMIT 0, 10;", nativeQuery = true)
    List<String> findAllRecordNamesOne(@Param("searchGroupBy") String searchGroupBy, @Param("recordName") String recordName);

    @Query(value = "SELECT record_name FROM (SELECT record_name, MAX(created_date) cd, COUNT(record_name) c FROM test_database.search_record WHERE search_group_by = ?1 AND record_name LIKE CONCAT('%', ?2, '%') GROUP BY record_name) AS rncdc ORDER BY c DESC, cd DESC LIMIT 0, 10;", nativeQuery = true)
    List<String> findAllRecordNamesTwo(@Param("searchGroupBy") String searchGroupBy, @Param("recordName") String recordName);
}