package com.shiminfxcvii.other;

import com.shiminfxcvii.entity.OperationLog;
import com.shiminfxcvii.entity.SearchRecord;
import com.shiminfxcvii.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.web.servlet.ModelAndView;

import java.time.format.DateTimeFormatter;

/**
 * @author ShiminFXCVII
 * @version 1.0
 * @description 统一管理常量
 * @class Constants
 * @since 2022/5/10 20:18 周二
 */
public enum Constants {
    ZERO_int(0),
    TEN_int(10),
    PAGE_NUM("pageNum"),
    PAGE_SIZE("pageSize"),
    DIRECTION("direction"),
    PROPERTY("property"),
    ZERO("0"),
    TEN("10"),
    ASC("ASC"),
    EMPLOYEES("employees"),
    CREATED_DATE("createdDate"),
    EMPLOYEE_ADDRESS("employeeAddress"),
    EMPLOYEE_ID("employeeId"),
    EMPLOYEE_ID_CARD("employeeIdCard"),
    EMPLOYEE_NAME("employeeName"),
    EMPLOYEE_MANAGEMENT("employee_management"),
    EMPLOYEE_PHONE_NUMBER("employeePhoneNumber"),
    SEARCH_GROUP_BY("searchGroupBy"),
    RECORD_NAME("recordName"),
    OPERATION_LOGS("operationLogs"),
    DATE_TIME("dateTime"),
    LOG_ID("logId"),
    X_CSRF_TOKEN("X-CSRF-Token"),
    FEMALE("女"),
    MALE("男"),
    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    PERSISTENT_LOGINS("persistent_logins"),
    TABLE(new String[]{"TABLE"}),
    DATE_TIME_FORMATTER(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
    B_CRYPT_PASSWORD_ENCODER(new BCryptPasswordEncoder()),
    TOKEN_REPOSITORY(new JdbcTokenRepositoryImpl()),
    MODEL_AND_VIEW(new ModelAndView()),
    OPERATION_LOG_ENTITY(new OperationLog()),
    SEARCH_RECORD(new SearchRecord()),
    USER(new User());

    private final Object value;

    Constants(Object value) {
        this.value = value;
    }

    public Object value() {
        return this.value;
    }
}