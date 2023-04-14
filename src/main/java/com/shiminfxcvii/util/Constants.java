package com.shiminfxcvii.util;

import org.springframework.http.HttpHeaders;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 统一管理常量
 *
 * @author ShiminFXCVII
 * @since 2022/5/10 20:18 周二
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
    String ID = "id";
    String EMPLOYEE_ID = "employeeId";
    String EMPLOYEE_NAME = "employeeName";
    String EMPLOYEE_ID_CARD = "employeeIdCard";
    String EMPLOYEE_ADDRESS = "employeeAddress";
    String EMPLOYEE_PHONE_NUMBER = "employeePhoneNumber";
    String CREATED_DATE = "createdDate";
    String EMPLOYEE_MANAGEMENT = "employee_management";
    String SEARCH_GROUP_BY = "searchGroupBy";
    String RECORD_NAME = "recordName";
    String RECORD_NAMES = "recordNames";
    String OPERATION_LOGS = "operationLogs";
    String DATE_TIME = "dateTime";
    String LOG_ID = "logId";
    String X_CSRF_TOKEN = "X-CSRF-Token";
    String INSERT = "INSERT";
    String UPDATE = "UPDATE";
    String DELETE = "DELETE";
    String PERSISTENT_LOGINS = "persistent_logins";
    String[] TABLE = {"TABLE"};
    /**
     * CTT -> Asia/Shanghai
     */
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.PRC)
            .withZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));
    HttpHeaders HTTP_HEADERS = new HttpHeaders();
    HttpHeaders ALL = new HttpHeaders();
    HttpHeaders JSON = new HttpHeaders();
    byte[] WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    char[] VALIDATE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
}