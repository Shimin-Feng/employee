package com.shiminfxcvii.util;

import com.shiminfxcvii.entity.OperationLog;
import com.shiminfxcvii.entity.SearchRecord;
import com.shiminfxcvii.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.web.servlet.ModelAndView;

import java.time.format.DateTimeFormatter;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description 统一管理常量
 * @interface Constants
 * @created 2022/5/10 20:18 周二
 */
public interface Constants {
    Integer ZERO_INTEGER = 0;
    Integer TEN_INTEGER = 10;
    String PAGE_NUM = "pageNum";
    String PAGE_SIZE = "pageSize";
    String DIRECTION = "direction";
    String PROPERTY = "property";
    String ZERO = "0";
    String TEN = "10";
    String ASC = "ASC";
    String EMPLOYEES = "employees";
    String CREATED_DATE = "createdDate";
    String EMPLOYEE_ADDRESS = "employeeAddress";
    String EMPLOYEE_ID = "employeeId";
    String EMPLOYEE_ID_CARD = "employeeIdCard";
    String EMPLOYEE_NAME = "employeeName";
    String EMPLOYEE_MANAGEMENT = "employee_management";
    String EMPLOYEE_PHONE_NUMBER = "employeePhoneNumber";
    String SEARCH_GROUP_BY = "searchGroupBy";
    String RECORD_NAME = "recordName";
    String OPERATION_LOGS = "operationLogs";
    String DATE_TIME = "dateTime";
    String LOG_ID = "logId";
    String X_CSRF_TOKEN = "X-CSRF-Token";
    String INSERT = "INSERT";
    String UPDATE = "UPDATE";
    String DELETE = "DELETE";
    String PERSISTENT_LOGINS = "persistent_logins";
    String[] TABLE = {"TABLE"};
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();
    HttpHeaders HTTP_HEADERS = new HttpHeaders();
    JdbcTokenRepositoryImpl TOKEN_REPOSITORY = new JdbcTokenRepositoryImpl();
    ModelAndView MODEL_AND_VIEW = new ModelAndView();
    OperationLog OPERATION_LOG_ENTITY = new OperationLog();
    SearchRecord SEARCH_RECORD = new SearchRecord();
    User USER = new User();
}
