package com.shiminfxcvii.service;

import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.model.cmd.EmployeeCmd;
import com.shiminfxcvii.model.query.EmployeeQuery;
import org.springframework.data.domain.Sort;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.security.Principal;

/**
 * EmployeeService
 *
 * @author ShiminFXCVII
 * @since 2022/10/3 22:51 周一
 */
public interface EmployeeService {

    /**
     * 当进入员工管理页面之前会立即执行一次查询所有员工，默认按照添加时间升序排列，每页十条数据
     *
     * @param model 页面模型对象
     * @author ShiminFXCVII
     * @since 2022/10/3 15:14
     */
    void employee(Model model);

    /**
     * 该方法有两个作用，添加和修改员工信息
     * 在这里使用的是 saveAndFlush() 而不是 save()，因为保存或者修改之后会立即查询数据库中该条数据
     * 如果使用 save() 可能会出现保存或者修改方法执行之后，立即查询数据库中该记录可能会出现不存在的情况
     * save()          将数据保存在内存中
     * saveAndFlush()  保存在内存中的同时同步到数据库
     * PATCH 只更新部分字段
     *
     * @param user    登录用户
     * @param cmd     前台传过来的需要添加或者修改的员工信息，根据是否存在 employeeId 判断该请求为添加还是修改
     *                <ul>
     *                 <li>
     *                 如果是添加请求，将会在执行添加 sql 之后立即查询数据库是否存在该员工信息
     *                 存在则返回 status 200 ”添加成功“，否则返回 status 500 ”添加失败“
     *                 </li>
     *                 <li>
     *                 如果是修改请求，将会在执行该请求之前查询该修改请求的所有字段是否与修改前相同，不相同则执行修改请求，
     *                 在执行修改 sql 之后会立即再次查询该请求所包含数据是否成功修改到书库
     *                 成功则返回 status 200 ”修改成功“，否则返回 400 "修改失败，因为员工信息没有任何改变"
     *                 </li>
     *                </ul>
     * @param request 请求信息
     * @return 响应信息
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @author ShiminFXCVII
     * @since 2022/10/3 15:14
     */
    ResponseEntity<String> saveOrUpdateEmployee(Principal user,
                                                EmployeeCmd cmd,
                                                RequestEntity<Employee> request) throws IllegalAccessException;

    /**
     * 根据员工 ID 删除员工信息
     *
     * @param user 获取登录用户信息
     * @param id   前台传过来的 employeeId
     * @return 响应信息
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @author ShiminFXCVII
     * @since 2022/10/3 15:36
     */
    ResponseEntity<String> deleteEmployeeById(Principal user, Long id) throws IllegalAccessException;

    /**
     * 根据条件和关键字搜索员工信息<br>
     * 除了首次进入员工管理页面前调用 employee()<br>
     * 其他查询请求全部改为进入该方法<br>
     * 就算前台不传值过来，@RequestBody Employee employee 也不会为空<br>
     * null == employee //false
     *
     * @param pageNum   返回该值所有页数数据，默认第 1 页
     * @param pageSize  该页数据显示条数，默认 10 条数据
     * @param direction 排序规则，ASC 升序，DESC 降序
     * @param property  根据该字段排序，默认 createdDate 添加时间
     *                  <ul>可用字段
     *                   <li>employeeName             员工姓名</li>
     *                   <li>employeeSex              性别</li>
     *                   <li>employeeAge              年龄</li>
     *                   <li>employeeIdCard           身份证号码</li>
     *                   <li>employeeAddress          住址</li>
     *                   <li>employeePhoneNumber      电话号码</li>
     *                   <li>createdBy                添加者</li>
     *                   <li>createdDate              添加时间</li>
     *                   <li>lastModifiedDate         最后操作时间</li>
     *                  </ul>
     * @param query     根据员工的某一个字段和值进行搜索
     * @param model     页面模型
     * @author ShiminFXCVII
     * @since 2022/10/3 16:31
     */
    void findEmployeesBy(Integer pageNum,
                         Integer pageSize,
                         Sort.Direction direction,
                         String property,
                         EmployeeQuery query,
                         Model model);

}