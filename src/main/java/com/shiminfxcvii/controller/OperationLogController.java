package com.shiminfxcvii.controller;

import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.entity.OperationLog;
import com.shiminfxcvii.repository.OperationLogRepository;
import com.shiminfxcvii.util.Sex;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.shiminfxcvii.util.Constants.*;
import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description 保存和查询操作员工信息后的日志
 * @class OperationLogController
 * @created 2022/5/2 1:15 周一
 * @see EmployeeController
 */
@Controller
@RequestMapping("operationLog")
public class OperationLogController {

    @Resource
    private OperationLogRepository operationLogRepository;

    /**
     * @param model Model 把值添加给页面模型
     * @return "operationLog" 映射到页面
     * @method operationLog
     * @author shiminfxcvii
     * @created 2022/5/4 11:42
     */
    @GetMapping
    public String operationLog(Model model) {
        Page<OperationLog> operationLogs = operationLogRepository.findAll(PageRequest.of(ZERO_INTEGER, TEN_INTEGER, Sort.by(DATE_TIME, LOG_ID)));
        // 数据库存储的性别是 0 和 1，但是需要使页面正常显示
        for (OperationLog operationLog : operationLogs)
            operationLog.setEmployeeSex(Objects.requireNonNull(Sex.resolveByNumber(operationLog.getEmployeeSex())).getGender());
        model.addAttribute(OPERATION_LOGS, operationLogs);
        return "operation-log";
    }

    /**
     * 保存操作员工信息后的日志
     * 在这里使用的是 saveAndFlush() 而不是 save()，因为保存或者修改之后会立即查询数据库中该条数据
     * 如果使用 save() 可能会出现保存或者修改方法执行之后，立即查询数据库中该记录可能会出现不存在的情况
     * save()          将数据保存在内存中
     * saveAndFlush()  保存在内存中的同时同步到数据库
     *
     * @param dml      INSERT, UPDATE, DELETE
     * @param employee 员工实体类
     * @param user     获取登录用户信息
     * @method saveOperationLog
     * @author shiminfxcvii
     * @created 2022/5/3 16:28
     */
    public void saveOperationLog(@NotNull String dml, @NotNull Employee employee, @NotNull Principal user) {
        OPERATION_LOG_ENTITY.setLogId(UUID.randomUUID().toString());
        OPERATION_LOG_ENTITY.setDml(dml);
        OPERATION_LOG_ENTITY.setEmployeeId(employee.getEmployeeId());
        OPERATION_LOG_ENTITY.setEmployeeName(employee.getEmployeeName());
        OPERATION_LOG_ENTITY.setEmployeeSex(employee.getEmployeeSex());
        OPERATION_LOG_ENTITY.setEmployeeAge(employee.getEmployeeAge());
        OPERATION_LOG_ENTITY.setEmployeeIdCard(employee.getEmployeeIdCard());
        OPERATION_LOG_ENTITY.setEmployeeAddress(employee.getEmployeeAddress());
        OPERATION_LOG_ENTITY.setEmployeePhoneNumber(employee.getEmployeePhoneNumber());
        OPERATION_LOG_ENTITY.setCreatedBy(employee.getCreatedBy());
        OPERATION_LOG_ENTITY.setCreatedDate(employee.getCreatedDate());
        OPERATION_LOG_ENTITY.setLastModifiedDate(employee.getLastModifiedDate());
        OPERATION_LOG_ENTITY.setUsername(user.getName());
        OPERATION_LOG_ENTITY.setDateTime(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        operationLogRepository.saveAndFlush(OPERATION_LOG_ENTITY);
        // 检查是否成功保存到数据库
        if (operationLogRepository.findById(OPERATION_LOG_ENTITY.getLogId()).isPresent()) {
            LOGGER.info("操作日志保存成功。");
        } else {
            LOGGER.error("操作日志保存失败。");
        }
    }

    /**
     * 根据条件查找操作日志
     *
     * @param pageNum  Integer 返回该值所有页数数据，默认第 1 页
     * @param pageSize Integer 该页数据显示条数，默认 10 条数据
     * @param model    Model 页面模型
     * @method findOperationLogsBy
     * @author shiminfxcvii
     * @created 2022/5/3 16:39
     */
    @GetMapping(value = "findOperationLogsBy", params = {PAGE_NUM, PAGE_SIZE}, headers = {CACHE_CONTROL, X_CSRF_TOKEN}, consumes = ALL_VALUE, produces = TEXT_HTML_VALUE)
    public String findOperationLogsBy(
            @RequestParam(value = PAGE_NUM, defaultValue = ZERO) Integer pageNum,
            @RequestParam(value = PAGE_SIZE, defaultValue = TEN) Integer pageSize,
            Model model
    ) {
        Page<OperationLog> operationLogs = operationLogRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by(DATE_TIME, LOG_ID)));
        // 数据库存储的性别是 0 和 1，但是需要使页面正常显示
        for (OperationLog operationLog : operationLogs)
            operationLog.setEmployeeSex(Objects.requireNonNull(Sex.resolveByNumber(operationLog.getEmployeeSex())).getGender());
        model.addAttribute(OPERATION_LOGS, operationLogs);
        return "operation-log";
    }
}