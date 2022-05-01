package com.example.controller;

import com.example.entity.Employee;
import com.example.entity.SearchRecord;
import com.example.methods.CustomMethods;
import com.example.repository.SearchRecordRepository;
import net.minidev.json.JSONArray;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.lang.System.out;

/**
 * @author $himin F
 * @version 1.0
 * @description 对搜索记录的保存与查询
 * @class SearchRecordController
 * @created 2022/5/1 15:54 周日
 */
@Controller
public class SearchRecordController {

    // 不设置编码前台就无法解析汉字
    private static final String ENCODE = "UTF-8";
    @Resource
    private SearchRecordRepository searchRecordRepository;

    /**
     * 保存用户的搜索记录，数据由查找员工信息时一并传到后台，由 EmployeeController.findEmployeesBy() 方法调用
     *
     * @param user     String 登录用户
     * @param employee Employee 实体类某单一字段和属性
     * @method saveRecordName
     * @author $himin F
     * @created 2022/4/30 11:11
     * @see com.example.controller.EmployeeController#findEmployeesBy(Integer, Integer, Sort.Direction, String, Employee, Principal, Model)
     */
    public void saveRecordName(Employee employee, Principal user) {
        if (null != user) {
            // 获取这个类的所有属性
            // 循环遍历所有的 fields
            for (Field field : employee.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (null != field.get(employee) && !Objects.equals(field.get(employee).toString(), "")) {
                        out.println(field.getName() + ": " + field.get(employee));
                        searchRecordRepository.save(new SearchRecord(UUID.randomUUID().toString(), field.getName(),
                                field.get(employee).toString(), user.getName(), new Date()));
                        break;
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 根据条件和关键字查找搜索记录，用于前台搜索框的 autocomplete，自动完成提示
     *
     * @param user          Principal 登录用户
     * @param searchGroupBy String 搜索依据
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
     * @param recordName    String 搜索关键字
     *                      将会根据该关键字执行 4 次查询，每两次搜索结果去重后整合为一个结果，最终返回给前台
     * @param response      HttpServletResponse 将要返回的状态和信息
     * @method findRecordNamesBy
     * @author $himin F
     * @created 2022/4/29 11:34
     */
    @RequestMapping("findRecordNamesBy")
    public void findRecordNamesBy(@NotNull Principal user, String searchGroupBy, String recordName, HttpServletResponse response) {
        String recordNames;
        // 查找此用户的搜索记录 ?%
        List<String> thisRecordNamesOne = searchRecordRepository.findThisRecordNamesOne(user.getName(), searchGroupBy, recordName);
        // 如果有十条则返回，否则继续查找
        if (10 == thisRecordNamesOne.size()) {
            recordNames = JSONArray.toJSONString(thisRecordNamesOne);
        } else {
            // 继续查找此用户的搜索记录 %?%
            List<String> thisRecordNamesTwo = searchRecordRepository.findThisRecordNamesTwo(user.getName(), searchGroupBy, recordName);
            // 合并并去重，前 ?% ———— %?% 后
            List<String> thisRecordNamesThree = CustomMethods.mergeTwoLists(thisRecordNamesOne, thisRecordNamesTwo);
            if (10 == thisRecordNamesThree.size()) {
                recordNames = JSONArray.toJSONString(thisRecordNamesThree);
            } else if (10 < thisRecordNamesThree.size()) {
                // 如果多于十条则只取前面十条数据
                recordNames = JSONArray.toJSONString(CustomMethods.getListTopTenData(thisRecordNamesThree));
            } else {
                // 所有用户的相关搜索记录，需要输入框有内容
                if (null != recordName && !Objects.equals(recordName, "")) {
                    List<String> allRecordNamesOne = searchRecordRepository.findAllRecordNamesOne(searchGroupBy, recordName);
                    List<String> thisAndAllRecordNamesOne = CustomMethods.mergeTwoLists(thisRecordNamesThree, allRecordNamesOne);
                    if (10 == thisAndAllRecordNamesOne.size()) {
                        recordNames = JSONArray.toJSONString(thisAndAllRecordNamesOne);
                    } else if (10 < thisAndAllRecordNamesOne.size()) {
                        // 如果多于十条则只取前面十条数据
                        recordNames = JSONArray.toJSONString(CustomMethods.getListTopTenData(thisAndAllRecordNamesOne));
                    } else {
                        List<String> allRecordNamesTwo = searchRecordRepository.findAllRecordNamesTwo(searchGroupBy, recordName);
                        List<String> thisAndAllRecordNamesTwo = CustomMethods.mergeTwoLists(thisAndAllRecordNamesOne, allRecordNamesTwo);
                        if (10 >= thisAndAllRecordNamesTwo.size()) {
                            recordNames = JSONArray.toJSONString(thisAndAllRecordNamesTwo);
                        } else {
                            // 如果多于十条则只取前面十条数据
                            recordNames = JSONArray.toJSONString(CustomMethods.getListTopTenData(thisAndAllRecordNamesTwo));
                        }
                    }
                } else {
                    recordNames = JSONArray.toJSONString(thisRecordNamesThree);
                }
            }
        }
        try {
            response.setCharacterEncoding(ENCODE);
            response.getWriter().write(recordNames);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}