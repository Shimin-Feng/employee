CREATE DATABASE IF NOT EXISTS employee_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE employee_management;

DROP TABLE IF EXISTS employee;

CREATE TABLE IF NOT EXISTS employee
(
    employee_id           VARCHAR(36) NOT NULL COMMENT '员工 id'
        PRIMARY KEY,
    employee_name         VARCHAR(45) NOT NULL COMMENT '员工姓名',
    employee_sex          VARCHAR(1)  NOT NULL COMMENT '员工性别',
    employee_age          VARCHAR(2)  NOT NULL COMMENT '员工年龄',
    employee_id_card      VARCHAR(18) NOT NULL COMMENT '员工身份证号码',
    employee_address      VARCHAR(45) NOT NULL COMMENT '员工住址',
    employee_phone_number VARCHAR(11) NOT NULL COMMENT '员工电话号码',
    created_by            VARCHAR(45) NOT NULL COMMENT '员工信息创建者',
    created_date          VARCHAR(19) NOT NULL COMMENT '员工信息创建时间',
    last_modified_date    VARCHAR(19) NOT NULL COMMENT '员工信息最后更改时间',
    CONSTRAINT employee_management_employee_employee_id_uindex
        UNIQUE (employee_id)
) COMMENT '员工信息表';

DROP TABLE IF EXISTS operation_logs;

CREATE TABLE IF NOT EXISTS operation_logs
(
    log_id                VARCHAR(36) NOT NULL COMMENT '操作记录 id'
        PRIMARY KEY,
    dml                   VARCHAR(6)  NOT NULL COMMENT 'dml 名称',
    employee_id           VARCHAR(36) NOT NULL COMMENT '员工 id',
    employee_name         VARCHAR(45) NOT NULL COMMENT '员工姓名',
    employee_sex          VARCHAR(1)  NOT NULL COMMENT '员工性别',
    employee_age          VARCHAR(2)  NOT NULL COMMENT '员工年龄',
    employee_id_card      VARCHAR(18) NOT NULL COMMENT '员工身份证号码',
    employee_address      VARCHAR(45) NOT NULL COMMENT '员工住址',
    employee_phone_number VARCHAR(11) NOT NULL COMMENT '员工电话号码',
    created_by            VARCHAR(45) NOT NULL COMMENT '员工信息创建者',
    created_date          VARCHAR(19) NOT NULL COMMENT '员工信息创建时间',
    last_modified_date    VARCHAR(19) NOT NULL COMMENT '员工信息最后更改时间',
    username              VARCHAR(45) NOT NULL COMMENT '操作用户',
    date_time             VARCHAR(19) NOT NULL COMMENT '操作时间',
    CONSTRAINT employee_management_operation_logs_log_id_uindex
        UNIQUE (log_id)
) COMMENT '员工管理操作日志表';

DROP TABLE IF EXISTS persistent_logins;

CREATE TABLE IF NOT EXISTS persistent_logins
(
    username  VARCHAR(64) NOT NULL COMMENT '用户账号',
    series    VARCHAR(64) NOT NULL COMMENT '序列号'
        PRIMARY KEY,
    token     VARCHAR(64) NOT NULL COMMENT 'token 值',
    last_used TIMESTAMP   NOT NULL COMMENT '最后使用时间'
) COMMENT '用户 token 表';

DROP TABLE IF EXISTS search_record;

CREATE TABLE IF NOT EXISTS search_record
(
    record_id       VARCHAR(36) NOT NULL COMMENT '搜索记录 id'
        PRIMARY KEY,
    search_group_by VARCHAR(45) NOT NULL COMMENT '搜索记录根据',
    record_name     VARCHAR(45) NOT NULL COMMENT '搜索记录名称',
    username        VARCHAR(45) NOT NULL COMMENT '搜索记录创建者',
    created_date    DATETIME    NOT NULL DEFAULT NOW() COMMENT '搜索记录生成时间',
    CONSTRAINT employee_management_search_record_record_id_uindex
        UNIQUE (record_id)
) COMMENT '搜索记录表';

DROP TABLE IF EXISTS user;

CREATE TABLE IF NOT EXISTS user
(
    user_id     VARCHAR(36) NOT NULL COMMENT '用户 id'
        PRIMARY KEY,
    username    VARCHAR(45) NOT NULL COMMENT '用户账号',
    password    VARCHAR(60) NOT NULL COMMENT '用户密码',
    authorities VARCHAR(45) NOT NULL COMMENT '用户权限（多个权限用","英文逗号隔开）',
    CONSTRAINT employee_management_user_user_id_uindex
        UNIQUE (user_id)
) COMMENT '用户表';