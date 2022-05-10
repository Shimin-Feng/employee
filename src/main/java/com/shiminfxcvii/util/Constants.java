package com.shiminfxcvii.util;

import com.shiminfxcvii.entity.OperationLog;
import com.shiminfxcvii.entity.SearchRecord;
import com.shiminfxcvii.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.web.servlet.ModelAndView;

import java.time.format.DateTimeFormatter;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description 统一管理常量
 * @class Constants
 * @created 2022/5/10 20:18 周二
 */
public record Constants() {
    public static final Integer STATUS_200 = 200;
    public static final Integer STATUS_400 = 400;
    public static final Integer STATUS_500 = 500;
    public static final String PAGE_NUM = "pageNum";
    public static final String PAGE_SIZE = "pageSize";
    public static final String DIRECTION = "direction";
    public static final String PROPERTY = "property";
    public static final String ZERO = "0";
    public static final String TEN = "10";
    public static final String ASC = "ASC";
    public static final String EMPLOYEES = "employees";
    public static final String CREATED_DATE = "createdDate";
    public static final String EMPLOYEE_ADDRESS = "employeeAddress";
    public static final String EMPLOYEE_ID = "employeeId";
    public static final String EMPLOYEE_ID_CARD = "employeeIdCard";
    public static final String EMPLOYEE_NAME = "employeeName";
    public static final String EMPLOYEE_MANAGEMENT = "employee_management";
    public static final String EMPLOYEE_PHONE_NUMBER = "employeePhoneNumber";
    public static final String SEARCH_GROUP_BY = "searchGroupBy";
    public static final String RECORD_NAME = "recordName";
    public static final String OPERATION_LOGS = "operationLogs";
    public static final String DATE_TIME = "dateTime";
    public static final String LOG_ID = "logId";
    public static final String X_CSRF_TOKEN = "X-CSRF-Token";
    public static final String FEMALE = "女";
    public static final String MALE = "男";
    public static final String INSERT = "INSERT";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";
    public static final String PERSISTENT_LOGINS = "persistent_logins";
    public static final String[] TABLE = {"TABLE"};
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();
    public static final JdbcTokenRepositoryImpl TOKEN_REPOSITORY = new JdbcTokenRepositoryImpl();
    public static final ModelAndView MODEL_AND_VIEW = new ModelAndView();
    public static final OperationLog OPERATION_LOG_ENTITY = new OperationLog();
    public static final SearchRecord SEARCH_RECORD = new SearchRecord();
    public static final User USER = new User();
}
