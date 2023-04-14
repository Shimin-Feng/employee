package com.shiminfxcvii.controller;

import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.entity.SearchRecord;
import com.shiminfxcvii.model.cmd.EmployeeCmd;
import com.shiminfxcvii.service.SearchRecordService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

import static com.shiminfxcvii.util.Constants.*;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 对搜索记录的保存与查询
 *
 * @author ShiminFXCVII
 * @since 2022/5/1 15:54
 */
@Controller
@RequestMapping("searchRecord")
public class SearchRecordController {

    private final SearchRecordService searchRecordService;

    public SearchRecordController(SearchRecordService searchRecordService) {
        this.searchRecordService = searchRecordService;
    }

    @PostMapping(
            value = "saveSearchRecord",
            headers = {CACHE_CONTROL, X_CSRF_TOKEN},
            consumes = ALL_VALUE,
            produces = ALL_VALUE
    )
    public ResponseEntity<String> saveSearchRecord(@NotNull(value = "用户信息不能为空") Principal user, EmployeeCmd cmd)
            throws IllegalAccessException {
        return searchRecordService.saveSearchRecord(user, cmd);
    }

    @DeleteMapping(
            value = "deleteByRecordName",
            params = {SEARCH_GROUP_BY, RECORD_NAME},
            headers = {CACHE_CONTROL, X_CSRF_TOKEN},
            consumes = ALL_VALUE,
            produces = ALL_VALUE
    )
    public ResponseEntity<String> deleteByRecordName(@NotNull(value = "用户信息不能为空") Principal user,
                                                     // TODO
                                                     @NotNull SearchRecord searchRecord/*,
                                                     @NotNull(value = "搜索字段不能为空") String searchGroupBy,
                                                     @NotNull(value = "搜索名称不能为空") String recordName*/) {
        return searchRecordService.deleteByRecordName(user, searchRecord);
    }

    @GetMapping(
            value = "findRecordNamesBy",
            params = {SEARCH_GROUP_BY, RECORD_NAME},
            headers = {CACHE_CONTROL, X_CSRF_TOKEN},
            consumes = ALL_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    // TODO: 为什么 recordName 中头和或尾有 % 就进不来接口？
    public ResponseEntity<Set<String>> findRecordNamesBy(@NotNull(value = "用户信息不能为空") Principal user,
                                                         @NotNull(value = "搜索字段不能为空") String searchGroupBy,
                                                         @RequestParam(value = "recordName", required = false)
                                                         String recordName) {
        return searchRecordService.findRecordNamesBy(user, searchGroupBy, recordName);
    }

    @GetMapping(
            value = "findAllPropertiesOfEmployeesBy",
            params = RECORD_NAMES,
            headers = {CACHE_CONTROL, X_CSRF_TOKEN},
            consumes = ALL_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Set<String>> findAllPropertiesOfEmployeesBy(
            @RequestParam(value = "recordName", required = false) String[] recordNames,
            // TODO
            @NotNull(value = "员工信息不能为空") Employee employee)
            throws IllegalAccessException {
        return searchRecordService.findAllPropertiesOfEmployeesBy(recordNames, employee);
    }

}