package com.shiminfxcvii.employee.service;

import com.shiminfxcvii.employee.entity.Employee;
import org.springframework.ui.Model;

import java.security.Principal;

/**
 * OperationLogService
 *
 * @author ShiminFXCVII
 * @since 2022/10/3 23:01 周一
 */
public interface OperationLogService {

    /**
     * 当进入操作记录页面之前会立即执行一次查询所有员工，默认按照添加时间升序排列，每页十条数据
     *
     * @param model 页面模型对象
     * @author ShiminFXCVII
     * @since 2022/10/3 20:14
     */
    void operationLog(Model model);

    /**
     * 保存操作员工信息后的日志
     *
     * @param dml      INSERT, UPDATE or DELETE
     * @param employee 员工实体类
     * @param user     获取登录用户信息
     * @throws IllegalAccessException 如果此 Field 对象正在执行 Java 语言访问控制并且基础字段不可访问。
     * @author ShiminFXCVII
     * @since 2022/10/3 20:22
     */
    void saveOperationLog(String dml, Employee employee, Principal user) throws IllegalAccessException;

    /**
     * 根据条件查找操作日志
     *
     * @param pageNum  返回该值所有页数数据，默认第 1 页
     * @param pageSize 该页数据显示条数，默认 10 条数据
     * @param model    页面模型
     * @author ShiminFXCVII
     * @since 2022/10/3 20:31
     */
    void findOperationLogsBy(Integer pageNum, Integer pageSize, Model model);

}