package com.shiminfxcvii.controller;

import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.entity.OperationLog;
import com.shiminfxcvii.repository.OperationLogRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description 保存和查询操作员工信息后的日志
 * @class OperationLogController
 * @created 2022/5/2 1:15 周一
 * @see EmployeeController
 */
@Controller
public class OperationLogController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static final Logger LOGGER = Logger.getGlobal();
    @Resource
    private OperationLogRepository operationLogRepository;

    /**
     * @param model Model 把值添加给页面模型
     * @return "operationLog" 映射到页面
     * @method operationLog
     * @author shiminfxcvii
     * @created 2022/5/4 11:42
     */
    @RequestMapping("operationLog")
    public String operationLog(@NotNull Model model) {
        model.addAttribute("operationLogs", operationLogRepository.findAll(
                PageRequest.of(0, 10, Sort.by("dateTime", "logId"))));
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
    public void saveOperationLog(String dml, @NotNull Employee employee, @NotNull Principal user) {
        OperationLog operationLog = new OperationLog();
        operationLog.setLogId(UUID.randomUUID().toString());
        operationLog.setDml(dml);
        operationLog.setEmployeeId(employee.getEmployeeId());
        operationLog.setEmployeeName(employee.getEmployeeName());
        operationLog.setEmployeeSex(employee.getEmployeeSex());
        operationLog.setEmployeeAge(employee.getEmployeeAge());
        operationLog.setEmployeeIdCard(employee.getEmployeeIdCard());
        operationLog.setEmployeeAddress(employee.getEmployeeAddress());
        operationLog.setEmployeePhoneNumber(employee.getEmployeePhoneNumber());
        operationLog.setCreatedBy(employee.getCreatedBy());
        operationLog.setCreatedDate(employee.getCreatedDate());
        operationLog.setLastModifiedDate(employee.getLastModifiedDate());
        operationLog.setUsername(user.getName());
        operationLog.setDateTime(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        operationLogRepository.saveAndFlush(operationLog);
        // 检查是否成功保存到数据库
        if (operationLogRepository.findById(operationLog.getLogId()).isPresent()) {
            LOGGER.info("操作日志保存成功。");
        } else {
            LOGGER.severe("操作日志保存失败。");
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
    @RequestMapping("findOperationLogsBy")
    public String findOperationLogsBy(
            @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @NotNull Model model
    ) {
        model.addAttribute(
                "operationLogs",
                operationLogRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by("dateTime", "logId")))
        );
        return "operation-log";
    }
}