package com.shiminfxcvii.employee.service;

import com.shiminfxcvii.employee.model.cmd.EmployeeCmd;
import com.shiminfxcvii.employee.entity.Employee;
import com.shiminfxcvii.employee.entity.SearchRecord;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Set;

/**
 * SearchRecordService
 *
 * @author ShiminFXCVII
 * @since 2022/10/3 23:09 周一
 */
public interface SearchRecordService {

    /**
     * 保存用户的搜索记录，数据由查找员工信息时一并传到后台
     *
     * @param user 登录用户
     * @param cmd  实体类某单一字段和属性
     * @return 响应信息
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @author ShiminFXCVII
     * @since 2022/10/3 20:41
     */
    ResponseEntity<String> saveSearchRecord(Principal user, EmployeeCmd cmd) throws IllegalAccessException;

    /**
     * 接受删除搜索记录请求
     *
     * @param user         登录用户
     * @param searchRecord 接收前台传递的值
     * @return 响应信息
     * @author ShiminFXCVII
     * @since 2022/10/3 20:52
     */
    ResponseEntity<String> deleteByRecordName(Principal user, SearchRecord searchRecord);

    /**
     * 根据条件和关键字查找搜索记录，用于前台搜索框的 autocomplete，自动完成提示
     *
     * @param user          登录用户
     * @param searchGroupBy 搜索字段
     *                      <ul>可用字段
     *                       <li>employeeName             员工姓名</li>
     *                       <li>employeeSex              性别</li>
     *                       <li>employeeAge              年龄</li>
     *                       <li>employeeIdCard           身份证号码</li>
     *                       <li>employeeAddress          住址</li>
     *                       <li>employeePhoneNumber      电话号码</li>
     *                       <li>createdBy                添加者</li>
     *                       <li>createdDate              添加时间</li>
     *                       <li>lastModifiedDate         最后操作时间</li>
     *                      </ul>
     * @param recordName    搜索关键字
     *                      将会根据该关键字执行 4 次查询，每两次搜索结果去重后整合为一个结果，最终返回给前台
     * @return 响应信息，并带有符合条件的搜索结果
     * @author ShiminFXCVII
     * @since 2022/10/3 21:07
     */
    ResponseEntity<Set<String>> findRecordNamesBy(Principal user, String searchGroupBy, String recordName);

    /**
     * 用于前台搜索框 autocomplete，在当前用户的当前搜索记录不足 10 条时执行搜索，在 employee 表中搜索相关字段的信息
     *
     * @param recordNames 前台搜索框的值，可以为 null
     * @param employee    根据有值的字段搜索，用于接受前台传过来的值
     * @return 响应信息，并带有符合条件的搜索结果
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @author ShiminFXCVII
     * @since 2022/10/3 21:14
     */
    ResponseEntity<Set<String>> findAllPropertiesOfEmployeesBy(String[] recordNames, Employee employee)
            throws IllegalAccessException;

}