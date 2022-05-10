package com.shiminfxcvii.controller;

import com.shiminfxcvii.entity.Employee;
import com.shiminfxcvii.repository.SearchRecordRepository;
import com.shiminfxcvii.util.ListMethods;
import net.minidev.json.JSONArray;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.shiminfxcvii.util.Constants.*;
import static com.sun.xml.internal.stream.writers.XMLStreamWriterImpl.UTF_8;
import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description 对搜索记录的保存与查询
 * @class SearchRecordController
 * @created 2022/5/1 15:54 周日
 */
@Controller
@RequestMapping("searchRecord")
public class SearchRecordController {

    @Resource
    private SearchRecordRepository searchRecordRepository;

    /**
     * 保存用户的搜索记录，数据由查找员工信息时一并传到后台，由 EmployeeController.findEmployeesBy() 方法调用<br>
     * 在这里使用的是 saveAndFlush() 而不是 save()，因为保存或者修改之后会立即查询数据库中该条数据
     * 如果使用 save() 可能会出现保存或者修改方法执行之后，立即查询数据库中该记录可能会出现不存在的情况
     * save()          将数据保存在内存中
     * saveAndFlush()  保存在内存中的同时同步到数据库
     *
     * @param user     String 登录用户
     * @param employee Employee 实体类某单一字段和属性
     * @throws IllegalAccessException 非法访问异常。通过反射访问对象属性时可能抛出
     * @method saveSearchRecord
     * @author shiminfxcvii
     * @created 2022/4/30 11:11
     * @see com.shiminfxcvii.controller.EmployeeController#findEmployeesBy(Integer, Integer, Sort.Direction, String, Employee, Principal, Model)
     */
    public void saveSearchRecord(Employee employee, Principal user) throws IllegalAccessException {
        if (null != user) {
            // 获取这个类的所有属性
            // 循环遍历所有的 fields
            for (Field field : employee.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (null != field.get(employee) && !Objects.equals(field.get(employee).toString(), "")) {
                    String recordId = UUID.randomUUID().toString();
                    SEARCH_RECORD.setRecordId(recordId);
                    SEARCH_RECORD.setSearchGroupBy(field.getName());
                    SEARCH_RECORD.setRecordName(field.get(employee).toString());
                    SEARCH_RECORD.setUsername(user.getName());
                    SEARCH_RECORD.setCreatedDate(LocalDateTime.now());
                    searchRecordRepository.saveAndFlush(SEARCH_RECORD);
                    if (searchRecordRepository.findById(recordId).isPresent()) {
                        LOGGER.info("搜索记录保存成功。");
                    } else {
                        LOGGER.error("搜索记录保存失败。");
                    }
                    break;
                }
            }
        }
    }

    /**
     * 接受删除搜索记录请求
     *
     * @param recordName 搜索记录 id
     * @param user       登录用户
     * @param response   返回响应
     * @throws IOException HttpServletResponse 写入响应信息异常
     * @method deleteByRecordName
     * @author shiminfxcvii
     * @created 2022/5/7 21:11
     */
    @DeleteMapping(value = "deleteByRecordName", params = {SEARCH_GROUP_BY, RECORD_NAME}, headers = {CACHE_CONTROL, X_CSRF_TOKEN}, consumes = ALL_VALUE, produces = ALL_VALUE)
    public void deleteByRecordName(Principal user, String searchGroupBy, String recordName, HttpServletResponse response) throws IOException {
        // 状态码
        int status;
        // 返回信息
        String message;
        // id 不允许为空
        if (null != recordName && null != user.getName()) {
            // 删除之前判断是否存在数据
            // 需要是属于该用户的搜索记录才允许被删除
            if (0 < searchRecordRepository.findThisRecordNamesBy(user.getName(), searchGroupBy, recordName)) {
                searchRecordRepository.deleteByRecordName(recordName);
                // 检查是否成功删除
                if (0 == searchRecordRepository.findThisRecordNamesBy(user.getName(), searchGroupBy, recordName)) {
                    status = STATUS_200;
                    message = "搜索记录删除成功。";
                } else {
                    status = STATUS_500;
                    message = "搜索记录删除失败，服务器出现故障。";
                }
            } else {
                status = STATUS_400;
                message = "搜索记录删除失败，该搜索记录不存在。可能在此之前已经被删除。";
            }
        } else {
            status = STATUS_400;
            message = "搜索记录删除失败，搜索记录名为空。";
        }
        response.setCharacterEncoding(UTF_8);
        response.setStatus(status);
        response.getWriter().write(message);
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
     * @throws IOException HttpServletResponse 写入响应信息异常
     * @method findRecordNamesBy
     * @author shiminfxcvii
     * @created 2022/4/29 11:34
     */
    @GetMapping(value = "findRecordNamesBy", params = {SEARCH_GROUP_BY, RECORD_NAME}, headers = {CACHE_CONTROL, X_CSRF_TOKEN}, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    public void findRecordNamesBy(@NotNull Principal user, String searchGroupBy, String recordName, HttpServletResponse response) throws IOException {
        List<String> recordNames;
        // 查找此用户的搜索记录 ?%
        List<String> thisRecordNamesOne = searchRecordRepository.findThisRecordNamesOne(user.getName(), searchGroupBy, recordName);
        // 如果有十条则返回，否则继续查找
        if (10 == thisRecordNamesOne.size()) {
            recordNames = thisRecordNamesOne;
        } else {
            // 继续查找此用户的搜索记录 %?%
            List<String> thisRecordNamesTwo = searchRecordRepository.findThisRecordNamesTwo(user.getName(), searchGroupBy, recordName);
            // 合并并去重，前 ?% ———— %?% 后
            List<String> thisRecordNamesThree = ListMethods.mergeTwoLists(thisRecordNamesOne, thisRecordNamesTwo);
            if (10 == thisRecordNamesThree.size()) {
                recordNames = thisRecordNamesThree;
            } else if (10 < thisRecordNamesThree.size()) {
                // 如果多于十条则只取前面十条数据
                recordNames = ListMethods.getListTopTenData(thisRecordNamesThree);
            } else {
                // 所有用户的相关搜索记录，需要输入框有内容
                if (null != recordName && !Objects.equals(recordName, "")) {
                    List<String> allRecordNamesOne = searchRecordRepository.findAllRecordNamesOne(searchGroupBy, recordName);
                    List<String> thisAndAllRecordNamesOne = ListMethods.mergeTwoLists(thisRecordNamesThree, allRecordNamesOne);
                    if (10 == thisAndAllRecordNamesOne.size()) {
                        recordNames = thisAndAllRecordNamesOne;
                    } else if (10 < thisAndAllRecordNamesOne.size()) {
                        // 如果多于十条则只取前面十条数据
                        recordNames = ListMethods.getListTopTenData(thisAndAllRecordNamesOne);
                    } else {
                        List<String> allRecordNamesTwo = searchRecordRepository.findAllRecordNamesTwo(searchGroupBy, recordName);
                        List<String> thisAndAllRecordNamesTwo = ListMethods.mergeTwoLists(thisAndAllRecordNamesOne, allRecordNamesTwo);
                        if (10 >= thisAndAllRecordNamesTwo.size()) {
                            recordNames = thisAndAllRecordNamesTwo;
                        } else {
                            // 如果多于十条则只取前面十条数据
                            recordNames = ListMethods.getListTopTenData(thisAndAllRecordNamesTwo);
                        }
                    }
                } else {
                    recordNames = thisRecordNamesThree;
                }
            }
        }
        response.setCharacterEncoding(UTF_8);
        response.getWriter().write(JSONArray.toJSONString(recordNames));
    }

}