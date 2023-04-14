package com.shiminfxcvii.service.impl;

import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.entity.OperationLog;
import com.shiminfxcvii.enums.Sex;
import com.shiminfxcvii.model.dto.OperationLogDTO;
import com.shiminfxcvii.repository.OperationLogRepository;
import com.shiminfxcvii.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.security.Principal;
import java.time.LocalDateTime;

import static com.shiminfxcvii.util.Constants.*;

/**
 * OperationLogServiceImpl
 *
 * @author ShiminFXCVII
 * @since 2022/10/3 23:03 周一
 */
@Service
@Slf4j
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogRepository operationLogRepository;

    @Lazy
    public OperationLogServiceImpl(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    /**
     * 当进入操作记录页面之前会立即执行一次查询所有员工，默认按照添加时间升序排列，每页十条数据
     *
     * @param model 页面模型对象
     * @author ShiminFXCVII
     * @since 2022/10/3 20:14
     */
    @Override
    public void operationLog(Model model) {
        Page<OperationLog> operationLogs = operationLogRepository.findAll(
                PageRequest.of(ZERO_INTEGER, TEN_INTEGER, Sort.by(DATE_TIME, LOG_ID)));

        // 数据库存储的性别是 0 和 1，但是需要使页面正常显示
        operationLogs.map(operationLog -> {
            OperationLogDTO dto = new OperationLogDTO();
            BeanUtils.copyProperties(operationLog, dto);
            dto.setEmployeeSex(Sex.getGenderByOrdinal(operationLog.getEmployeeSex()));
            return dto;
        });
        model.addAttribute(OPERATION_LOGS, operationLogs);
    }

    /**
     * 保存操作员工信息后的日志
     * 在这里使用的是 saveAndFlush() 而不是 save()，因为保存或者修改之后会立即查询数据库中该条数据
     * 如果使用 save() 可能会出现保存或者修改方法执行之后，立即查询数据库中该记录可能会出现不存在的情况
     * save()          将数据保存在内存中
     * saveAndFlush()  保存在内存中的同时同步到数据库
     *
     * @param dml      INSERT, UPDATE or DELETE
     * @param employee 员工实体类
     * @param user     获取登录用户信息
     * @throws IllegalAccessException 如果此 Field 对象正在执行 Java 语言访问控制并且基础字段不可访问。
     * @author ShiminFXCVII
     * @since 2022/10/3 20:22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOperationLog(String dml, Employee employee, Principal user) throws IllegalAccessException {
        if (!StringUtils.hasText(user.getName())) {
            log.error("操作日志保存失败，原因：登录用户名不能为空但为空。");
            return;
        }

        for (Field field : employee.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            // 在这里不能用 StringUtils.hasText(String.valueOf(obj)) 判断是否有值，因为 null 会被转换成 String
            if (ObjectUtils.isEmpty(field.get(employee))) {
                log.error("操作日志保存失败，原因：实体类每个字段都不能为空，但 " + field.getName() + " 为空。");
                return;
            }
        }

        OperationLog operationLog = new OperationLog();
        operationLog.setDml(dml);
        operationLog.setEmployeeId(employee.getId());
        operationLog.setEmployeeName(employee.getEmployeeName());
        operationLog.setEmployeeSex(employee.getEmployeeSex());
        operationLog.setEmployeeAge(employee.getEmployeeAge());
        operationLog.setEmployeeIdCard(employee.getEmployeeIdCard());
        operationLog.setEmployeeAddress(employee.getEmployeeAddress());
        operationLog.setEmployeePhoneNumber(employee.getEmployeePhoneNumber());
        operationLog.setUsername(user.getName());
        operationLog.setDateTime(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        operationLogRepository.saveAndFlush(operationLog);

        // 检查是否成功保存到数据库
        if (operationLogRepository.findById(operationLog.getId()).isPresent())
            log.info("操作日志保存成功。");
        else
            log.error("操作日志保存失败。");
    }

    /**
     * 根据条件查找操作日志
     *
     * @param pageNum  返回该值所有页数数据，默认第 1 页
     * @param pageSize 该页数据显示条数，默认 10 条数据
     * @param model    页面模型
     * @author ShiminFXCVII
     * @since 2022/10/3 20:31
     */
    @Override
    public void findOperationLogsBy(Integer pageNum, Integer pageSize, Model model) {
        Page<OperationLog> operationLogs = operationLogRepository.findAll(
                PageRequest.of(pageNum, pageSize, Sort.by(DATE_TIME, LOG_ID)));

        // 数据库存储的性别是 0 和 1，但是需要使页面正常显示
        operationLogs.map(operationLog -> {
            OperationLogDTO dto = new OperationLogDTO();
            BeanUtils.copyProperties(operationLog, dto);
            dto.setEmployeeSex(Sex.getGenderByOrdinal(operationLog.getEmployeeSex()));
            return dto;
        });
        model.addAttribute(OPERATION_LOGS, operationLogs);
    }

}