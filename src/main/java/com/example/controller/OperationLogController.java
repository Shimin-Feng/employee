package com.example.controller;

import com.example.entity.Employee;
import com.example.entity.OperationLog;
import com.example.repository.OperationLogRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author $himin F
 * @version 1.0
 * @description
 * @class OperationLogController
 * @created 2022/5/2 1:15 周一
 * @see EmployeeController
 */
@Controller
public class OperationLogController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static final Logger LOGGER = Logger.getGlobal();
    @Resource
    private OperationLogRepository repository;

    public void saveOperationLog(String dml, @NotNull Employee employee, @NotNull Principal user) {
        OperationLog operationLogs = new OperationLog();
        operationLogs.setLogId(UUID.randomUUID().toString());
        operationLogs.setDml(dml);
        operationLogs.setEmployeeId(employee.getEmployeeId());
        operationLogs.setEmployeeName(employee.getEmployeeName());
        operationLogs.setEmployeeSex(employee.getEmployeeSex());
        operationLogs.setEmployeeAge(employee.getEmployeeAge());
        operationLogs.setEmployeeIdCard(employee.getEmployeeIdCard());
        operationLogs.setEmployeeAddress(employee.getEmployeeAddress());
        operationLogs.setEmployeePhoneNumber(employee.getEmployeePhoneNumber());
        operationLogs.setCreatedBy(employee.getCreatedBy());
        operationLogs.setCreatedDate(employee.getCreatedDate());
        operationLogs.setLastModifiedDate(employee.getLastModifiedDate());
        operationLogs.setUsername(user.getName());
        operationLogs.setDateTime(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        repository.saveAndFlush(operationLogs);
        // 检查是否成功保存到数据库
        if (repository.findById(operationLogs.getLogId()).isPresent()) {
            LOGGER.info("保存成功。");
        } else {
            LOGGER.warning("保存失败。");
        }
    }
}