package com.shiminfxcvii.util;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description 统一管理常量
 * @interface Constants
 * @created 2022/5/10 20:18 周二
 */
public sealed interface Constants permits ListMethods {
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
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.PRC).withZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));
}
